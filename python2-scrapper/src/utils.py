import requests
import urllib
from re import sub
import os
from io import BytesIO
from PIL import Image


def download_file(url, dest_dir):
    try:
        filename = url.rsplit('/', 1)[-1].partition('?')[0]
        request = requests.get(url)
        image = Image.open(BytesIO(request.content))
        if image.mode in ('RGBA', 'LA'):
            background = img_alpha_to_colour(image)
            image = background
        image.save(os.path.join(dest_dir, filename))
    except Exception, e:
        print str(e)


def img_alpha_to_colour(image, color=(255, 255, 255)):
    image.load()  # needed for split()
    background = Image.new('RGB', image.size, color)
    background.paste(image, mask=image.split()[3])  # 3 is the alpha channel
    return background


def get_file_urls(site_url, soup):
    file_urls = []

    # fix href errors
    for anchor in soup.find_all('img'):
        if anchor.get('src') is not None:
            anchor = anchor.get('src')
            if '//' not in anchor:
                file_urls.append('{}{}{}'.format(site_url,"/", anchor))
            elif 'http' not in anchor and 'https' not in anchor:
                file_urls.append(sub(r"//", "https://", anchor))
            else:
                file_urls.append(anchor)
    return file_urls


def get_largest_file_url(file_urls):
    file_size_dict = {}
    largest_file_url = ""
    for url in file_urls:
        try:
            file_size_dict[url] = int(urllib.urlopen(url).info()['Content-Length'])
        except Exception, e:
            print(str(e))
    if len(file_size_dict) is not 0:
        largest_file_url = max(file_size_dict, key=file_size_dict.get)
    return largest_file_url, max(file_size_dict.values())
