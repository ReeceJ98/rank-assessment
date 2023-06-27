package com.rank.assignment;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rank.assignment.dto.request.NewTransactionRequestDto;
import com.rank.assignment.dto.response.PlayerResponseDto;
import com.rank.assignment.dto.response.NewTransactionResponseDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.util.Assert;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AssignmentApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	public void shouldReturnBalance() throws Exception {
		ResultActions resultActions = mockMvc.perform(get("/player/{playerId}/balance", 1))
				.andDo(print())
				.andExpect(status().isOk());

		MvcResult result = resultActions.andReturn();
		String contentAsString = result.getResponse().getContentAsString();

		PlayerResponseDto playerResponseDto = objectMapper.readValue(contentAsString, PlayerResponseDto.class);

		Assert.isTrue(playerResponseDto.getBalance() > 0, "invalid balance");
		Assert.isTrue(playerResponseDto.getPlayerId() == 1, "incorrect id");

	}

	@Test
	public void shouldReturnTransaction() throws Exception {
		NewTransactionRequestDto newTransactionRequestDto = new NewTransactionRequestDto("win", 10);
		ResultActions resultActions = mockMvc.perform(post("/player/{playerId}/balance/update", 2)
						.content(asJsonString(newTransactionRequestDto))
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isOk());

		MvcResult result = resultActions.andReturn();
		String contentAsString = result.getResponse().getContentAsString();

		NewTransactionResponseDto newTransactionResponseDto = objectMapper.readValue(contentAsString, NewTransactionResponseDto.class);

		Assert.isTrue(newTransactionResponseDto.getTransactionId() > 0, "No valid transaction id");
		Assert.isTrue(newTransactionResponseDto.getBalance() > 0, "incorrect balance");

	}

	private static String asJsonString(final Object obj) {
		try {
			return new ObjectMapper().writeValueAsString(obj);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
