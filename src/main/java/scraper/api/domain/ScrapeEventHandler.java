package scraper.api.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.core.annotation.HandleAfterCreate;
import org.springframework.data.rest.core.annotation.HandleAfterSave;
import org.springframework.data.rest.core.annotation.HandleBeforeSave;
import org.springframework.data.rest.core.annotation.HandleBeforeCreate;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import scraper.api.amqp.TaskMessage;
import scraper.api.amqp.TaskProducer;

import java.util.Date;

@RepositoryEventHandler(Scrape.class)
public class ScrapeEventHandler
{
    @Autowired
    private TaskProducer taskProducer;

    @HandleBeforeCreate
    public void handleScrapeCreate(Scrape scrape)
    {
        scrape.setCreated(new Date());
        scrape.setUrl(scrape.getUrl().trim());
        scrape.setStatus("In Progress");

    }

    @HandleAfterCreate
    public void handleAfterScrapeCreate(Scrape scrape)
    {
        final TaskMessage taskMessage = new TaskMessage();
        taskMessage.setUrl(scrape.getUrl());
        taskProducer.sendNewTask(taskMessage);
    }

    @HandleAfterSave
    public void handleAfterScrapeSave(Scrape scrape)
    {
        final TaskMessage taskMessage = new TaskMessage();
        taskMessage.setUrl(scrape.getUrl());
        taskProducer.sendNewTask(taskMessage);
    }

    @HandleBeforeSave
    public void handleBeforeScrapeSave(Scrape scrape)
    {
        scrape.setCreated(new Date());
        scrape.setUrl(scrape.getUrl().trim());
        scrape.setStatus("In Progress");
    }

}