package com.travel.travelmonk.evals;

import com.travel.travelmonk.eval.ReviewService;
import com.travel.travelmonk.eval.Sentiment;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class SentimentAnalysisTest {

    @Autowired
    ReviewService reviewService;

    @Test
    public void testSentimentAnalysis() {
        String positiveReview = "I love this hotel, It was amazing experience";
        Sentiment positiveSentiment = reviewService.classifySentiment(positiveReview);
        assertEquals(Sentiment.POSITIVE, positiveSentiment);

        String negativeReview = "This is the worst experience I`ve ever had with this Hotel.";
        Sentiment negativeSentiment = reviewService.classifySentiment(negativeReview);
        assertEquals(Sentiment.NEGATIVE, negativeSentiment);

        String neutralReview = "This hotel is good, but can be better";
        Sentiment neutralSentiment = reviewService.classifySentiment(neutralReview);
        assertEquals(Sentiment.NEUTRAL, neutralSentiment);
    }
}
