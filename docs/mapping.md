## Mapping html
To load a specific html file when accessing the relevant url:

```
  @RequestMapping("/pagename")
  String pagename() {
    return "pagename";
  }
```
To pass data from Java/Spring to HTML:

```
model.addAttribute("currentorders", myList);
```

Example use in Java file:
```
  @RequestMapping("/waiterorderpage")
  public String getCurrentOrders(Model model) {
    List<String> myList = new ArrayList(); //could be data from database
    myList.add("Test");
    model.addAttribute("currentorders", myList);
    return "waiterorderpage"; //waiterorderpage is the name of the html file you want to load
  }
```

In the HTML file, you can access this using:

```
<p th:text="${currentorders}">Current orders</p>
```
Make sure th is declared at the top:
```
<html lang="en" xmlns:th="http://www.thymeleaf.org"
```