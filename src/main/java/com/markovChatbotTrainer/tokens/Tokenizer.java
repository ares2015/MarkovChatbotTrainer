package com.markovChatbotTrainer.tokens;

import java.util.List;

/**
 * Created by Oliver on 4/25/2017.
 */
public interface Tokenizer {

    List<String> splitStringIntoList(String sentence);

    String removeCommaAndDot(final String token);

}
