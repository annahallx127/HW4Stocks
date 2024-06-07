Our Program:
- Allows a user to examine the gain or loss of a stock over a valid specified period.
- Allows a user to examine the x-day moving average of a stock for a valid specified date and a
specified value of x.
- Allows a user to determine which days are x-day crossovers for a valid specified
stock over a specified date range and a specified value of x.
- Allows a user to create one or more portfolios with multiple shares of one or more stock, and find
the value of that portfolio on a previous date.

Users cannot:
 - Create a stock not found in the AlphaVantage database.
 - Create a portfolio with a stock that is not in the database.
 - Project the future value of a stock or portfolio, as stock options are not supported.
 - Use the program to make any trades, as the data is only updated with the close price of any
 given day.

Entering an invalid command will result in an error message, but the program will continue to run.

Note: The program will not be able to update the database if the user is not connected to the
internet. As such, data may become outdated if the user does not update the database regularly.
The program comes with a preloaded database (AAPL and GOOG) for demonstration purposes, but
any other stocks must be queried by the user.