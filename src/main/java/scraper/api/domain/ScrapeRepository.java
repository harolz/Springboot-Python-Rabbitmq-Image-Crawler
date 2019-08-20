package scraper.api.domain;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.List;

@RepositoryRestResource(collectionResourceRel = "scrapes", path = "scrapes")
public interface ScrapeRepository extends CrudRepository<Scrape, Long>
{
    @RestResource(path="url")
    List<Scrape> findByUrl(@Param("text") String url);
}
/**
 *
 *
 *  @GetMapping("/scrapes")
 *    public List<Scrape> retrieveAllScrapes() {
 * 		return ScrapeRepository.findAll();
 *    }
 *
 *
 *  @DeleteMapping("/scrapes/{id}")
 * 	public void deleteScrape(@PathVariable long id) {
 * 		ScrapeRepository.deleteById(id);
 * 	}
 *
 *  @PostMapping("/scrapes")    *
 *
 *  public ResponseEntity<Object> createScrape(@RequestBody Scrape scrape) {
 * 		Scrape savedScrape = ScrapeRepository.save(scrape);
 *
 * 		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
 * 				.buildAndExpand(savedScrape.getId()).toUri();
 *
 * 		return ResponseEntity.created(location).build();
 *    * 	}
 *
 *  @PutMapping("/scrapes/{id}")
 * 	public ResponseEntity<Object> updateScrape(@RequestBody Scrape scrape, @PathVariable long id) {
 *
 * 		Optional<Scrape> scrapeOptional = scrapeRepository.findById(id);
 *
 * 		if (!scrapeOptional.isPresent())
 * 			return ResponseEntity.notFound().build();
 *
 * 		scrape.setId(id);
 *
 * 		ScrapeRepository.save(scrape);
 *
 * 		return ResponseEntity.noContent().build();
 * 	}
 *
 *
 *
 */


