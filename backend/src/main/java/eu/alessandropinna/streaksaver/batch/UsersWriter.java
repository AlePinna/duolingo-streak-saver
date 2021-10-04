package eu.alessandropinna.streaksaver.batch;

import eu.alessandropinna.streaksaver.domain.User;
import eu.alessandropinna.streaksaver.service.BuyItemService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class UsersWriter implements ItemWriter<User> {

    @Autowired
    BuyItemService buyItemService;

    @Override
    public void write(List<? extends User> list) {

        for (var user : list.stream()
                .filter(x -> x.isBuyStreakFreeze())
                .collect(Collectors.toList())) {

            String logMessage = null;
            Integer httpStatus = null;

            try {

                ResponseEntity response = buyItemService.buyStreakFreeze(user);
                logMessage = response.getStatusCode().is2xxSuccessful()
                        ? "Successfully bought a streak freeze for user {}"
                        : "An error occurred buying a streak freeze for user {} - Status code {}";
                httpStatus = response.getStatusCodeValue();

            } catch(Exception e){
                logMessage = "An error occurred buying a streak freeze for user {}";
            }
            log.info(logMessage, user.getEmail(), httpStatus);
        }
    }
}
