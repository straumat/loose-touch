# DynamoDB.

## Primary key.
Each item in a table is uniquely identified by a primary key.

There are two types of primary key: 

  - Using a simple **primary key** is similar to standard key-value stores like Memcached or accessing rows in a SQL table by a primary key. One example would be a Users table with a Username primary key.

  - The **composite primary key** is more complex. With a composite primary key, you specify both a partition key and a sort key. The sort key is used to sort items with the same partition. One example could be an Orders tables for recording customer orders on an e-commerce site. The partition key would be the CustomerId, and the sort key would be the OrderId. You can only have one item with a particular combination of partition key and sort key.
  
## Secondary Indexes.

There are two types of secondary indexes :

 - The first kind of secondary index is a local secondary index. A local secondary index uses the same partition key as the underlying table but a different sort key. To take our Order table example from the previous section, imagine you wanted to quickly access a customer's orders in descending order of the amount they spent on the order. You could add a local secondary index with a partition key of CustomerId and a sort key of Amount, allowing for efficient queries on a customer's orders by amount.
   
 - The second kind of secondary index is a global secondary index. A global secondary index can define an entirely different primary key for a table. This could mean setting an index with just a partition key for a table with a composite primary key. It could also mean using completely different attributes to populate a partition key and sort key. With the Order example above, we could have a global secondary index with a partition key of OrderId so we could retrieve a particular order without knowing the CustomerId that placed the order.