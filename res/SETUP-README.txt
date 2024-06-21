Setup using our program!

For the text interface:
java -jar HW4Stocks.jar -text

For the GUI:
java -jar HW4Stocks.jar

Any other command line arguments will result in an error.
Double-clicking on the jar file should work.





very very last resort:
You can run the program by placing the jar file into a new folder, then in the search bar of that
folder you can delete the file directory and type "cmd" and enter
this will lead you to the command prompt window, and you can type
you might need the res folder.
java -jar HW4Stocks.jar



Reminder:
*After every input you enter press enter/return!*

HOW TO CREATE A PORTFOLIO WITH 3 DIFFERENT STOCKS:

When you run the program you will see 6 options.
- Enter the number 4 to create a new portfolio
- You will see a prompt to name it, go ahead and give it whatever name you'd like
- Next, enter the ticker symbol of what stock you would like to purchase
- Now enter the quantity of how many shares.
- Once you have entered the first stock in, you will be prompted to add more stocks into it
- Add the second and third one using the same steps from above
- Make sure the ticker for the stock is a valid ticker!

Congrats! Now you have created your own portfolio with 3 different stocks!!

HOW TO QUERY THE VALUE AND COST BASIS OF THE PORTFOLIO ABOVE ON TWO SPECIFIC DATES
- Now that you have created a new portfolio, you will have to go to the portfolio screen, enter 5
- Enter the number of which your portfolio that you just created is next to

Now you will be entered into the Portfolio menu screen with 9 options.
- To query the value of the portfolio at a specific date, choose option 6 (find portfolio value),
then enter the date you want. After this cycle, you will be prompted back to the portfolio menu
- To query the cost basis of that portfolio at a specific date, you can navigate to option 7 to
find the distribution of value of each stock at that time or go to option 4 to view the
composition of the portfolio at the given date
- Now that you are back to the menu screen, you can check the values with a different date by
going through the cycle again
- when you are done choose option 9 and then 7, which will quit the program for you!

Congrats!!

Stocks our program supports: All the stocks that are supported by the API (listing_status.csv)
All dates for portfolios should be supported including holidays that are New Years or
Christmas. If the holiday is not either of the two, the program will find the nearest market
date backwards.

However, if you want to re-balance, our program does not support re-balancing on a weekend or non
market date, this is specified in the program!
You also cannot enter any date that is before the latest transaction!

All dates for stock methods should be supported, if it is not a valid market date, for holidays such as
Christmas, or New Years, it will find the nearest market date backwards.