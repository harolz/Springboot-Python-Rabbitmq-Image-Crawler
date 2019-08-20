import os
import requests
from bs4 import BeautifulSoup
from urlparse import urlparse
import time
from utils import get_file_urls, get_largest_file_url, download_file


class ScrapingResult:
    def __init__(self):
        self.url = None
        self.file_summary = None
        self.status = None


class Scraper:

    def scrape(self, url):
        try:
            # get image
            print "Retrieving largest image of %s... " % url
            dest_dir = os.path.dirname(os.getcwd()) + '/scraped_imgs/' + urlparse(url).netloc + '/' + time.strftime('%Y-%m-%d %H:%M:%S')
            if os.name=="nt":
                dest_dir = os.path.dirname(os.getcwd()) + '\\scraped_imgs\\' + urlparse(url).netloc + '\\' + time.strftime('%Y-%m-%d %H-%M-%S')
            print dest_dir
            if not os.path.exists(dest_dir):
                os.makedirs(dest_dir)
            html = requests.get(url).text
            soup = BeautifulSoup(html, "html.parser")
            file_urls = get_file_urls(url, soup)
            largest_file_url, size= get_largest_file_url(file_urls)
            print "Downloading largest image file: " + largest_file_url
            download_file(largest_file_url, dest_dir)
            file_summary = 'Largest Image File: '+str(largest_file_url) + ' Sized: ' + str(size)
            status = "Success"

        except Exception, e:
            file_summary = "Scrape Failed. Reason: %s" % str(e)
            status = "Failure"
        print "Done: %s = %s" % (url, file_summary)
        # create scraping result
        scraping_result = ScrapingResult()
        scraping_result.summary = file_summary
        scraping_result.url = url
        scraping_result.status = status
        return scraping_result






