package com.oss.pages.servicedesk;

import com.oss.pages.BasePage;
import org.openqa.selenium.WebDriver;

public class TicketDetailsPage extends BasePage {

    public static final String DETAILS_PAGE_URL_PATTERN = "%s/#/view/service-desk/trouble-ticket/details/%s";

    public TicketDetailsPage(WebDriver driver) {
        super(driver);
    }
}
