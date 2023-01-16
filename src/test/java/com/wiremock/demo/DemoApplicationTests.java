package com.wiremock.demo;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.common.ConsoleNotifier;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.matching.RequestPatternBuilder;
import com.github.tomakehurst.wiremock.recording.RecordSpec;
import com.github.tomakehurst.wiremock.recording.RecordingStatus;
import com.wiremock.demo.config.WireMockConfig;
import com.wiremock.demo.service.UniService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@ActiveProfiles(value = "iTest")
@SpringBootTest
@AutoConfigureMockMvc
class DemoApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private WireMockConfig proxy;

	@Autowired
	private UniService uniService;

	protected final ObjectMapper objectMapper = new ObjectMapper();

	WireMockServer wireMockServer;


	@BeforeEach
	void startRecording() {
		wireMockServer = new WireMockServer(
				WireMockConfiguration.options()
						.port(proxy.getPort())
						.notifier(new ConsoleNotifier(true))
		);
		wireMockServer.start();
		if (proxy.isRecording()) {
			wireMockServer.startRecording(config(proxy.getUrl(), true));
		}
	}

	@AfterEach
	void stopRecording() {
		if (wireMockServer.getRecordingStatus().getStatus().equals(RecordingStatus.Recording)) {
			wireMockServer.stopRecording();
		}
		wireMockServer.stop();

	}


	@ParameterizedTest
	@CsvSource({"india"})
	void getUniversitiesForCountry(String country) throws Exception {
		String actualResponse = mockMvc.perform(get("/api/university")
						.contentType("application/json")
						.param("country", country)
				)
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
	}

	private RecordSpec config(String recordingURL, boolean recordingEnabled) {
		return WireMock.recordSpec()
				.forTarget(recordingURL)
				.onlyRequestsMatching(RequestPatternBuilder.allRequests())
				.captureHeader("Accept")
				.makeStubsPersistent(recordingEnabled)
				.ignoreRepeatRequests()
				.matchRequestBodyWithEqualToJson(true, true)
				.build();
	}

}
