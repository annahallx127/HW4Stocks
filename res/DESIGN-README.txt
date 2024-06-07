The design of our program is roughly based on the model view controller (MVC) architecture.

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
of the portfolio.

The model is represented by the 'Model' interface, and its implementation is the 'ModelImpl' class.
It stores any data that the user would work with, including any stocks or portfolios that
the user has queried or created. Stocks and Portfolios are stored in a HashMap for ease of access by
other methods. It is the only part of the program that interacts with the database and API,
allowing for the controller to be completely independent of the data source. The model only calls
the API when it needs to update the stock data (such as when it is not present in the database,
or the dates are not found within the stored file) or when the user requests a new stock. Thus,
users can use previously stored data without the need for an internet connection. However, due to
the fact that the data is stored in a file, the data is not updated in real time, and may be
inaccurate if used without updating the data.

The view is represented by the 'View' interface, and its implementation is the 'ViewImpl' class in
the view package. It is the user interface that the user interacts with, displaying any relevant
data to the user. However, the current design of the program also has the view handling user input,
which is not the intended design of the MVC architecture. The view currently directly manipulates
the model, which is also a consequence of the lack for SOLID design principles.

The controller is represented by the 'Controller' interface, and its implementation is the
'ControllerImpl' class in the controller package.
Due to the current design of the program, the controller does not function as a true controller as
stated in the MVC architecture. The controller is not responsible for handling user input.
Currently, the controller is only responsible for calling the main method to run the program.

The 'Main' class is the main method of the program, and is found within the
It creates a new instance of the model, view, and controller, and runs the program by calling the
controller's run method.