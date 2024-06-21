(See line 50 for the changes and additions).

(See line 102 for Part 3).

The design of our program is based on the model view controller (MVC) architecture.
Stocks are represented by the 'Stock' interface and implemented in the 'modelStock' class of
the model package. They represent a single stock for a company, and store the stock's symbol, name,
and the stock data for the stock. The stock data is stored in an ArrayList<String>, where each
individual day of stock data is stored in an element of the list. Each element stores a date,
open price, high price, low price, close price, and volume for that day. These elements are split by
commas in the file that the data is read from, and methods that parse the data use a regex to split
along this pattern. The methods in the stock class allow for the retrieval of the stock data to find
different pieces of information for a given day or date range.

Portfolios are represented by the 'Portfolio' interface and implemented in the 'modelPortfolio'
class of the model package. They have a name of the portfolio, and a HashMap of stocks that are in
the portfolio. The key for the HashMap is the stock symbol, and the value is the number of shares
of that stock that are in the portfolio. The portfolio can also calculate the total value of the
portfolio, based on the stock prices on any given day. Numerous methods to manipulate the portfolio
are present, such as adding stocks, removing stocks, and calculating the total value
of the portfolio. See below for the new design explanation!

The model is represented by the 'Model' interface, and its implementation is the 'ModelImpl' class.
It stores any data that the user would work with, including any stocks or portfolios that
the user has queried or created. Stocks and Portfolios are stored in a HashMap for ease of access by
other methods. It is the only part of the program that interacts with the database and API,
allowing for the controller to be completely independent of the data source. The model only calls
the API when it needs to update the stock data (such as when it is not present in the database,
or the dates are not found within the stored file) or when the user requests a new stock. Thus,
users can use previously stored data without the need for an internet connection. However, due to
the fact that the data is stored in a file, the data is not updated in real time, and may be
inaccurate if used without being updated (such as without an internet connection.)

The view is represented by the 'View' interface, and its implementation is the 'ViewImpl' class in
the view package. The view now controls the appendable and displays messages to the user
by appending them to a provided Appendable object. (Changes, we thought that would be a better
design than before)

The controller is represented by the 'Controller' interface, and its implementation is the
'ControllerImpl' class in the controller package. It is the part of the program that connects the
model and view, and is responsible for handling any user input. It calls the model to update the
data, and then calls the view to display the data to the user. Any exceptions that are thrown are
passed to the view to display an error message to the user.

Any and all data found from the API is stored in the 'data' package. The file name for a given stock
ticker is the stock ticker followed by ".csv".

The 'Main' class and method are found in the controller package, and are responsible for starting
the program. It creates a new instance of the controller, and then calls the go() method to start
the program. It will read and append from Readable and Appendable objects, defaulting to the
console.

WHAT WE CHANGED OR ADDED:
We fixed our Controller so that the bulk of the implementation would not be in the View. We added
Commands that our controller would call for specific interactions with the user. We added to the
Portfolio Interface and ModelPortfolio class to implement the new requirements of the portfolio.
We acknowledge that this isn't the best way and that we could have made a new interface that
extended the old interface and done code reuse, however, we interpreted the assignment through
thinking that the additions would add on to the program, making it a better version. Much like a
version update of an iPhone. Once you update the program, it is a better program, runs smoother,
way better than the old program that you will want to stick with the new version. That is how
we interpreted and implemented the assignment!

We added the new requirements to ModelPortfolio where now the user is able to query the value of
the portfolio at any date, add and remove stocks at a specific date, re-balance their portfolio
at a any date, find the value distribution and composition of their portfolio at any date. It
also supports saving users portfolios to the application and loading user portfolios in the
specified format. Our program does not support re-balancing on the weekends, the user will have to
enter a valid market date for them to re-balance their portfolio. Our model also now supports
fractional shares, however, the user is not allowed to explicitly purchase or sell fractional shares.
The only way for the user to have fractional shares is through re-balancing! The re-balance method
should now check that the target weights are valid or that they add up to 100.

The model now has ModelTransaction class and Transaction interface which logs the type of transaction
made by the user. This also helps keep track of the dates of each transaction to ensure
no transactions are made before the latest transaction (a design decision we made to minimize
conflicts).
The class contains useful methods that are called on by the portfolio. It can get the stock, and
shares of each type of transaction that has been made.

We added a parser so that the program could read portfolios and write portfolios for the user to save
and load whenever. We created the StockWriter interface, Stock Reader abstract class,
PortfolioReader and PortfolioWriter that work together to write and read XML files (chosen format).

The design of the parser separates the tasks of reading and writing stock portfolio data into
XML files, making the code more organized and easier to manage. It uses interfaces to define the
standard methods for these operations, allowing different implementations if needed. PortfolioWriter
writes the portfolio data to a temporary XML file and then formats it for readability, while
PortfolioReader parses an XML file to reconstruct the portfolio data. This separation ensures that
reading and writing are handled efficiently and independently. Overall, the design focuses on
modularity, flexibility, and error handling to provide a robust solution for managing stock
portfolio data in XML format.

To plot the performance bar chart, we created an enum called PlotInterval.
The PlotInterval enum defines various intervals for plotting data on a chart, such as days,
weeks, months, and years, along with multi-year intervals like five and
ten years. Each interval has a baseRows value associated with it, which likely corresponds
to how the data is grouped for plotting purposes. The scale method calculates the base scale for
each asterisk on the x-axis of the plot chart, using the total value of the portfolio and a
predefined target value (targetFirstValueAsterisks). This design allows for flexible plotting
of data across different time intervals and ensures that the plot scales appropriately based on
the total portfolio value. The use of enums makes it easy to manage and extend the intervals if
needed.

(Part 3)
ADDITIONS
We created a GUI called JFrameView that extends JFrame. The GUI allows users to create their
own portfolio and buy/sell stocks, find value of the portfolio at a given date, and find the
composition of the file at a given date. It also allows the user to save and load their portfolios.
They can load portfolios through their computer as long as the specified format is followed.
The UI is user-friendly and easy to follow, on the left there are a list of available portfolios.
If it is empty, the user can select the options below of either creating a new portfolio or
loading in their own. Once the portfolio is created, they can then query values and make
transactions in that portfolio when they click on the name. To the right, there is a live update
feed that repeats the actions of the user and reports the calculations the user has made. The
program is closed upon exiting the main menu window. We thought was the best design because the
user is able to evaluate what kind of actions they can take during the early steps of running the
program. The user will not have to go looking for anything as most of the stuff is displayed at
the get go. To go with the new view/gui, we created a new controller, implemented in
JFrameControllerImpl. The model stayed the same because we would be using the same methods to
query the values. The controller implementation is what will speak to the model on what to
calculate/how to calculate and then the final result will then be displayed.