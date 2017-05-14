# GridResource

This class   


## methods

Name | Return Value | Description
------------ | ------------- | -------------
[**main**](GridResource#main) | parseToJson | calculates concentration of ants |


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





