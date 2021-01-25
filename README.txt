Murad Berhanu 100996375

This project summarizes Wikipedia articles using. The code is based on Babluki’s text summarization algorithm and uses RESTful web services.

Start by connecting the project to a Tomcat server

The project uses Maven dependencies to get Jsoup

Send url requests in the format http://localhost:8080/TextSummarizerSystem/rest/ts/ followed by the ending of a wikipedia url after the "wiki" part of the url (e.g. http://localhost:8080/TextSummarizerSystem/rest/ts/Ottawa/)

You can add a summary length (number of sentence) if you want, at the end of the url e.g. http://localhost:8080/TextSummarizerSystem/rest/ts/Ottawa/15 --> 15 sentences

Sample page: https://github.com/muradberhanu/TextSummarizerSystem/blob/master/Example_summarized_page.png
