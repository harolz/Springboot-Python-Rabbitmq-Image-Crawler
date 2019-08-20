package scraper.api.amqp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import scraper.api.domain.Scrape;
import scraper.api.domain.ScrapeRepository;

import java.util.List;

@Component
public class ScrapingResultHandler
{
    @Autowired
    private ScrapeRepository scrapeRepository;

    public void handleMessage(ScrapingResultMessage scrapingResultMessage)
    {
        System.out.println("Received summary: " + scrapingResultMessage.getSummary());
        final String url = scrapingResultMessage.getUrl();
        final List<Scrape> scrapes = scrapeRepository.findByUrl(url);
        if (scrapes.size() == 0)
        {
            System.out.println("No scrape of url: " + url + " found.");
        }
        else
        {
            for (Scrape scrape : scrapes)
            {
                scrape.setSummary(scrapingResultMessage.getSummary());
                scrape.setStatus(scrapingResultMessage.getStatus());
                scrapeRepository.save(scrapes);
                System.out.println("updated scrape: " + url);
            }
        }
    }
}