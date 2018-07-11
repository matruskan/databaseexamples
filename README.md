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

The [InefficientComputation.java](src/main/java/com/matruskan/databaseexamples/InefficientComputation.java)
class shows an example of **moving computation to the DBMS**.

## 2. Unnecessary Computation

"More than 10% of the performance issues are caused by (mis)using
ORM APIs that lead to unnecessary queries being issued."

- "**Loop-invariant queries**. Sometimes, queries are repeatedly
issued to load the same database contents and hence are unnecessary."
- "**Dead-store queries**. In such cases, queries are repeatedly issued
to load different database contents into the same memory
object while the object has not been used between the reloads.
- "**Queries with known results**. A number of issues are due to
issuing queries whose results are already known, hence incurring
unnecessary network round trips and query processing time.

The [UnnecessaryComputation.java](src/main/java/com/matruskan/databaseexamples/UnnecessaryComputation.java)
class shows an example of **loop-invariant queries**.
