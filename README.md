# Database Examples

Some Dos and Don'ts based on this post: https://blog.acolyer.org/2018/06/28/how-_not_-to-structure-your-database-backed-web-applications-a-study-of-performance-bugs-in-the-wild/ ,
which is about the study "How _not_ to structure your database-backed web applications: a study of performance bugs in the wild" Yang et al., _ICSE'18_.

Although this project uses Hibernate, the tips are valid for applications that use other ORM frameworks.

## 1. Inefficient Computation

"The poorly performing code conducts useful computation but inefficiently"

- "**Inefficient queries**. The same operation on persistent data can
be implemented via different ORM calls. However, the performance
of the generated queries can be drastically different."
- "**Moving computation to the DBMS**. As the ORM framework
hides the details of query generation, developers often write code
that results in multiple queries being generated."
- "**Moving computation to the server**. Interestingly, there are
cases where the computation should be moved to the server from
the DBMS."

The [InefficientComputation.java](src/main/java/com/matruskan/databaseexamples/InefficientComputation.java) example shows an example of **moving computation to the DBMS**.
