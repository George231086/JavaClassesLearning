# JSoupMessing

Messing around with JSoup and JDBC. Java training.

The purpose of this program was to get more experience with Java database connectivity, a little webscraping and to generate a big table that I can use to do some analysis on with pandas in Python. 

The program is a small gui application that allows a url to be scanned for particular tags and the results displayed in a text area. The results can also be uploaded to a database with the update database button. The text as well as the date is scraped and the href if available will be uploaded. There is an auto update radio button which when selected will update the database every minute. Another button "Show latest db entries" displays the most recent 100 entries in another text area. Finally there is a button to export the database to a text file for further analysis.

The database I used was Apache Derby in netbeans. Can supply the db location, username, password, db class Driver and tablename to the constructor. The default is given by, ScannerFrame("jdbc:derby://localhost:1527/sample","app","app","org.apache.derby.jdbc.ClientDriver","APP.Test"). The table needs to already exist. 

The url by default is http://www.dailymail.co.uk/home/index.html and the tag is h2. This isn't because I'm a fan of the daily mail, it was chosen primarily due to the simplicity of it's page structure. The autoupdate feature essentially captures all their headlines whilst it's running, allowing them to be analysed later. When testing the app with different urls and tags some useless info was undoubtedly entered into the db table. This was somewhat welcome as in the future when I start looking at it with Python one part of the analysis will be removing the noise.




