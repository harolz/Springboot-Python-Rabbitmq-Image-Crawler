
// The scrape model
function Scrape(selfHref, url, created, status, summary) {
    var self = this;
    self.selfHref = selfHref;
    self.url = ko.observable(url);
    self.created = created;
    self.status = status;
    self.summary = summary;
    self.editing = ko.observable(false);

    // Behaviors
    self.edit = function() { this.editing(true) }
}

// The scrape view model
function ScrapeViewModel() {
    var self = this;

    self.newUrl = ko.observable();
    self.scrapes = ko.observableArray([]);


    // add scrape: send POST to scrapes resource
    self.addScrape = function () {
        // a little bit of pre-processing of user entered url 
        var newUrl = self.newUrl();
        if (typeof newUrl == "undefined") {
            alert("Url required");
            return;
        }

        // prefix with http:// if not added by user
        if (newUrl.search(/^http[s]?\:\/\//) == -1) {
            newUrl = 'http://' + newUrl;
        }



        // make POST request
        $.ajax("http://localhost:8080/scrapes", {
            data: '{"url": "' + newUrl +  '"}',
            type: "post",
            contentType: "application/json",
            success: function (allData) {
                self.loadScrapes();
                self.newUrl("");
            }
        });
    };

    // update scrape: send PUT to existing scrapes resource
    self.updateScrape = function (scrape) {

        // same as in "addScrape" a little bit of parameter checking. Some code duplication here
        // but we leave it for demonstration purposes
        var newUrl = scrape.url();
        if (typeof newUrl == "undefined") {
            alert("Url required");
            return;
        }

        // prefix with http:// if not added by user
        if (newUrl.search(/^http[s]?\:\/\//) == -1) {
            newUrl = 'http://' + newUrl;
        }


        // make PUT request (or send PATCH then we don't need to include the created date)
        $.ajax(scrape.selfHref, {
            data: '{"url": "' + newUrl +'"}',
            type: "put",
            contentType: "application/json",
            success: function (allData) {
                self.loadScrapes();
            }
        });
    };


    // delete scrape: send DELETE to scrapes resource
    self.deleteScrape = function (scrape) {
        $.ajax(scrape.selfHref, {
            type: "delete",
            success: function (allData) {
                self.loadScrapes();
            }
        });
    };

    // load scrapes from server: GET on scrapes resource
    self.loadScrapes = function () {
        $.ajax("http://localhost:8080/scrapes", {
            type: "get",
            success: function (allData) {
                var json = ko.toJSON(allData);
                var parsed = JSON.parse(json);
                if (parsed._embedded) {
                    var parsedScrapes = parsed._embedded.scrapes;
                    var mappedScrapes = $.map(parsedScrapes, function (scrape) {
                        return new Scrape(scrape._links.self.href, scrape.url, scrape.created, scrape.status, scrape.summary)
                    });
                    self.scrapes(mappedScrapes);
                } else {
                    self.scrapes([]);
                }

            }
        });
    };

    // Load initial data
    self.loadScrapes();
}




ko.applyBindings(new ScrapeViewModel());