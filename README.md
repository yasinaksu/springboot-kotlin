# Political Speech Analysis Application

## Summary
The purpose of this task is to derive various statistics from provided data about political speeches. The application needs to process UTF-8 encoded CSV files, sourced externally via HTTP, which are structured as follows:
```csv
Speaker;Topic;Date;Words
Alexander Abel; education policy; 2012-10-30, 5310
Bernhard Belling; coal subsidies; 2012-11-05; 1210
Caesare Collins; coal subsidies; 2012-11-06; 1119
Alexander Abel; homeland security; 2012-12-11; 911
```
# Technologies
- Java 17
- Kotlin 1.9.23
- Spring boot 3.2.5
- Restful API
- JUnit5
- Docker
- Apache Commons Csv 1.10.0

# Prerequisites
- Maven
- Docker

## Installation

### Git clone
```bash
git clone https://github.com/yasinaksu/springboot-kotlin.git
```

# Run & Build


There are 2 ways of run & build the political-speech application.
### 1. Docker ###

To build and run `political-speech` service via docker

```sh 
$ cd political-speech
$ docker build -t political-speech-app . 
$ docker run -d --name political-speech-app -p 8080:8080 political-speech-app
```

### 3. Maven ###
```sh
$PORT 8080       
```
To build and run `political-speech` service via maven

```sh
$ cd political-speech
$ mvn clean install
$ mvn spring-boot:run
```

# Usage of the political-speech application

### Evaluate Statistics : HTTP GET method

You can send multiple URLs as query parameters in the format as follows

`"/evaluation?url=url1&url=url2`

The CSV files located at these URLs are evaluated and, if the input is valid, the following questions should be answered:

1. Which politician gave the most speeches in 2013?
2. Which politician gave the most speeches on "homeland security"?
3. Which politician spoke the fewest words overall?

### The output should be as JSON in this format:

``` js
{
 "mostSpeeches": null,
 "mostSecurity": "Alexander Abel",
 "leastWordy": "Caesare Collins"
}

```

### Note 1: If no or no unique answer is possible for a question, this field should be filled with null.

### Note 2: If there is any server-side error in the system, an error is returned in the following format:

``` js
{
    "status": 500,
    "message": "Something bad wrong please reach the system administrator"
}
```

### Note 3: If the CSV file is not available at the provided addresses, an error is returned as follows:

``` js
{
    "status": 400,
    "message": "CSV file not found at URL: http://localhost:3000/static/nofile.csv"
}
```

### Note 4: If there is a format error in the CSV file from the source, such as missing headers, an error is returned as follows:

``` js
{
    "status": 400,
    "message": "CSV must include headers: speaker, topic, date, words"
}
```
### Note 5: The CSV file is based on the format specified in the test case document, and a semicolon (;) is used as the delimiter. If a different delimiter is used or there is a format error, an error response is expected as follows:

``` js
{
    "status": 400,
    "message": "Failed to parse CSV due to malformed format please use semicolon ';' as delimiter Index for header 'Topic' is 1 but CSVRecord only has 1 values!"
}
```