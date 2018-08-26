
# ngram-analyzer
## How to use

 - Download 1gram (a-z) files from google [archive](http://storage.googleapis.com/books/ngrams/books/datasetsv2.html) for the language of your choice.
 - Extract these files into a folder, say, `extractedFolder`
 - Clone this project and open `ngram-analyzer/1gramanalyzerapp/dist` folder in terminal.
 - run `java -jar 1gramAnalyzerApp.jar`.
 - Now click on `Select Folder` button to select the `extractedFolder`
 - Click on `Generate Word List` button to start
 - Watch the logs. Once `Done`, it will generate the word list into  `extractedFolder/word_list.txt` file.
