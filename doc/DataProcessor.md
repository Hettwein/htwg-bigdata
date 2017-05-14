# DataProcessor

This class contains methods to calculate the concentration of the ants within the Grid and builds a JSON Document to transfer the data to the front-end visualisation.  


## methods

Name | Return Value | Description
------------ | ------------- | -------------
[**transformGrid**](DataProcessor.md#transformGrid) | parseToJson | calculates concentration of ants |
[**laodRDD**](DataProcessor.md#loadRDD) | MongoSpark | creates Connection to MongoDB and sets Configurations |
[**parseToJson**](DataProcessor.md#parseToJson) | Json | parses GridRepresentattion to Json |
[**extractConfig**](DataProcessor.md#extractConfig) | RDD |  |
[**extractAntPos**](DataProcessor.md#extractAntPos) | RDD |  |
[**filterCurrentPos**](DataProcessor.md#filterCurrentPos) | Boolean |  |


# **transformGrid**
> DataProcessor transformGrid (gridRequest: GridRequest)

Description

### parameters
Name | Type | Description
------------- | ------------- | -------------
 **GridRequest** | **GridRequest**| Contains a Collection of Strings, coordinates x (int) and y (int) and a timestep (int) |

### Return type
[**String**]



# **loadRDD**
> DataProcessor loadRDD (gridRequest: GridRequest)

Defines the SparkContext and the Configuration for the Database. 

### parameters
Name | Type | Description
------------- | ------------- | -------------
 **GridRequest** | **gridRequest**| requested Grid for visualisation |


### Return type
[**MongoRDD[Document]**]





# **parseToJson**
> DataProcessor parseToJson (gridRep: List[GridRepresentation])

The method parseToJson parse a List of GridRepresentation. GridRepresentation is a case class with the params 
step: Int 
time: Long 
fields: Array[Document])

### parameters
Name | Type | Description
------------- | ------------- | -------------
 **gridRep** | **List[GridRepresentation]**| List of class GridRepresentation (step: Int, time: Long, fields: Array[Document]) |

### Return type
[**String**]



# **extractConfig**
> DataProcessor extractConfig (rdd: MongoRDD[Document])

Extracts the Config-Document out of the database.

### parameters
Name | Type | Description
------------- | ------------- | -------------
**rdd** | **MongoRDD[Document]**|  |


### Return type
[**MongoRDD[Document]**]



# **extractAntPos**
> DataProcessor extractAntPos (rdd: MongoRDD[Document])

Extracts all positions of the Ants out of the database.

### parameters
Name | Type | Description
------------- | ------------- | -------------
**rdd** | **MongoRDD[Document]**|  |


### Return type
[**MongoRDD[Document]**]




# **filterCurrentPos**
> DataProcessor filterCurrentPos (gridRequest: GridRequest,doc: Document, currentMillis: Long)

Filters all positions and finds the current position of each Ant.

### parameters
Name | Type | Description
------------- | ------------- | -------------
**gridRequest** | **GridRequest**|  |
**doc** | **Document**|  |
**currentMillis** | **long**| timestamp in milliseconds |


### Return type
**true**: if the position within the document is the current position of the ant 
**false**: if the position within the document isn't the current position of the ant




