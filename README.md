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

## 3. Inefficient Data Accessing

"Problems under this category suffer from data transfer slow
downs, including not batching data transfers (e.g., the well-known
“N+1” problem) or batching too much data into one transfer."

- "**Inefficient lazy loading**. [...] when a set
of objects O in table T1 are requested, objects stored in table T2
associated with T1 and O can be loaded together through eager
loading. If lazy loading is chosen instead, one query will be issued
to load N objects from T1, and then N separate queries have to
be issued to load associations of each such object from T2. This is
known as the “N+1” query problem."
- "**Inefficient eager loading**. However, always loading data eagerly
can also cause problems. Specifically, when the associated
objects are too large, loading them all at once will create huge
memory pressure and even make the application unresponsive."
- "**Inefficient updating**. Like the “N+1” problem, developers would
issue N queries to update N records separately rather than merging them
into one update."

The [InefficientDataAccessing.java](src/main/java/com/matruskan/databaseexamples/InefficientDataAccessing.java)
class shows an example of **inefficient lazy loading**, and an example of
**inefficient eager loading**.
