import pika
import json
from scraper import Scraper

credentials = pika.PlainCredentials("guest", "guest")
parameters = pika.ConnectionParameters(host='localhost', credentials=credentials)

connection = pika.BlockingConnection(parameters)
channel = connection.channel()
tasks_queue = channel.queue_declare(queue='tasks.queue', durable=True)
scraping_result_queue = channel.queue_declare(queue='scrapingresult.queue', durable=True)

print' [*] Waiting for tasks. To exit press CTRL+C'
print tasks_queue
print scraping_result_queue


def publish_result(scraping_result):
    j = json.dumps(scraping_result.__dict__)
    print j
    properties = pika.BasicProperties(content_type="application/json")
    channel.basic_publish(exchange='', routing_key='scrapingresult.queue', body=j, properties=properties)


def callback(ch, method, properties, body):
    url = json.loads(body)['url']
    scraper = Scraper()
    result = scraper.scrape(url.strip())
    publish_result(result)


channel.basic_consume("tasks.queue", callback, auto_ack=True)
channel.start_consuming()
