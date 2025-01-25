import * as AWS  from 'aws-sdk'
import { DocumentClient } from 'aws-sdk/clients/dynamodb'
import { UserItem } from '../models/UserItem'
import { FoodItem } from '../models/FoodItem'
import { createLogger } from '../utils/logger'

const logger = createLogger('Eat Restaurant DataAcess')
import Jimp from 'jimp/es';

export class EatRestaurantAccess {

  constructor(
    //Create a DynamoDB client
    private readonly docClient: DocumentClient = createDynamoDBClient(),

    //Retrieve the Evironment Variables For all the Resources    
    private readonly userPhonePasswordIndex = process.env.USER_PHONE_PASSWORD_INDEX,
    private readonly userRestaurantTable = process.env.USERS_RESTAURANT_TABLE,
    // private readonly foodIndex = process.env.FOOD_INDEX,
    private readonly foodRestaurantTable = process.env.FOOD_RESTAURANT_TABLE,
    private readonly bucketName = process.env.EAT_RESTAURANT_S3_BUCKET,
    private readonly expires = process.env.SIGNED_URL_EXPIRATION,        
    // private readonly thumbnailBucketName = process.env.THUMBNAILS_S3_BUCKET,
    private readonly region = process.env.BUCKET_REGION
  ) {}

  async createDemoUser(): Promise<boolean> {

    let error: boolean = false;

    const tableName = this.userRestaurantTable


    logger.info("tableName", {tableName} )                       


    const addItems = [            
      {              
        PutRequest: {
          Item: {
            "isStaff": true,
            "name": "Alan Tang Sing Lun",
            "password_hash": "enVrYXlubw==",
            "phone": "85266966379",
            "secureCode": "1234",
            "userId": "1"
          }
        }
      }                                             
    ]  


    const jsonInsert =  {
        RequestItems: {
          [tableName]: [...addItems]
        }
      }   

      await this.docClient.batchWrite(jsonInsert).promise()
      .then(() => {
        logger.info('Items added', {added: 'item added'})
      })
      .catch((e) => {
        logger.info('Failed to add', {added: e.message}) 
        error = true
      })

      return error
    } 

 async getUser(phone: string, password: string): Promise<UserItem[]> {

      var params = {
        TableName: this.userRestaurantTable,  
        IndexName: this.userPhonePasswordIndex,
        KeyConditionExpression:  "phone = :phone and password_hash = :password_hash",      
        ExpressionAttributeValues: {
            ":phone": phone,
            ":password_hash": password
        }
      };

      const result = await this.docClient.query(params).promise();
      const items = result.Items
      logger.info('getUser', items)

      return items as UserItem[]
  }  

  async getFoodItems(foodId: String): Promise<FoodItem[]> {

    const params = {
      TableName: this.foodRestaurantTable,
      ProjectionExpression: "id, #name, description, image, price, discount, menuid",
      FilterExpression: "menuid = :foodId",
      ExpressionAttributeNames:{
        "#name": "name"
      },    
      ExpressionAttributeValues: {
          ":foodId": foodId
      }
    }
  

    const result = await this.docClient.scan(params).promise();
    const items = result.Items
    logger.info('getFoodItems', items)

    return items as FoodItem[]
}  

async getMaxId(): Promise<FoodItem[]> {

  const params = {
    TableName: this.foodRestaurantTable,
    ProjectionExpression: "id, #name, description, image, price, discount, menuid", 
    ExpressionAttributeNames:{
      "#name": "name"
    }
  }


  const result = await this.docClient.scan(params).promise();
  const items = result.Items
  logger.info('getMaxId', items)

  return items as FoodItem[]
} 

  async checkUser(phone :string, password :any): Promise<Boolean> {

    var params = {
      TableName: this.userRestaurantTable,  
      IndexName: this.userPhonePasswordIndex,
      KeyConditionExpression:  "phone = :phone and password_hash = :password_hash",      
      ExpressionAttributeValues: {
          ":phone": phone,
          ":password_hash": password
      }
    };

    const result = await this.docClient.query(params).promise();
    const items = result.Items
    logger.info('phone', {phone})
    logger.info('password', {password})
    logger.info('checkUser', items)

    if (items.length > 0)
        return true

    return false
  }

  async checkPhone(phone :string): Promise<Boolean> {

    const params = {
      TableName: this.userRestaurantTable,
      ProjectionExpression: "userId, #name, phone, password_hash, isStaff, secureCode",
      FilterExpression: "phone = :phone",  
      ExpressionAttributeNames:{
        "#name": "name"
      },   
      ExpressionAttributeValues: {
          ":phone": phone
      }
    }

    const result = await this.docClient.scan(params).promise()
    const items = result.Items

    if (items.length > 0)
        return true

    return false
  }

  async createFood(food: FoodItem): Promise<FoodItem> {     

    const params = {
       TableName: this.foodRestaurantTable,
       Item: food
     }
     
     await this.docClient.put(params).promise();
 
     return food
   }

   async createUser(user: UserItem): Promise<UserItem> {     

    const params = {
       TableName: this.userRestaurantTable,
       Item: user
     }
     
     await this.docClient.put(params).promise();
 
     return user
   }

   async editFood(food: FoodItem): Promise<FoodItem> {     

    var params = {
      TableName: this.foodRestaurantTable,
      Key: { id : food.id},
      UpdateExpression: 'set #name = :x , description = :u , price = :d, discount = :o, image = :i ',
      ExpressionAttributeNames: {'#name' : 'name'},
      ExpressionAttributeValues: {
        ':x' : food.name,
        ':u' : food.description,
        ':d' : food.price,
        ':o' : food.discount,
        ':i' : food.image
      },
      ReturnValues: "ALL_NEW"
    };

     await this.docClient.update(params).promise();
 
     return food
   }

   async deleteFood(foodId: string): Promise<boolean> { 
    
    
    //Parameters For deleting the User Todo'S Records.
    var params = {
      TableName: this.foodRestaurantTable,
      Key:{
          "id": foodId,       
      }
    };

    await this.docClient.delete(params).promise();

    return true;

  }

   getUploadUrl(foodId: string): string {

    //This part generates the presigned URL for the S3 Bucket.
    const s3 = new AWS.S3({
      region: this.region,
      params: {Bucket: this.bucketName}
    });    

    var params = {Bucket: this.bucketName, Key: foodId, Expires: parseInt(this.expires)};

    logger.info('UrlUpload Param', params)
    
    return s3.getSignedUrl('putObject', params)
 
  }

  async uploadImageToS3(key: string, imageBitMap: any): Promise<boolean> {

    logger.info('Processing S3 item with key: ', {key})
    // logger.info('Buffer',{imageBuffer: imageBitMap})

    const image = await Jimp.read(imageBitMap)
  
    // logger.info('Buffer',{imageBuffer: image})
  
    const convertedBuffer = await image.getBufferAsync(Jimp.MIME_JPEG)

    logger.info('Buffer',{imageBuffer: convertedBuffer})
    
    const s3 = new AWS.S3({
      region: this.region,
      params: {Bucket: this.bucketName}
    });  
  
    logger.info('Writing image back to S3 bucket', {bucket: this.bucketName})
    await s3
      .putObject({
        Bucket: this.bucketName,
        Key: `${key}.jpeg`,
        Body: convertedBuffer,
        ContentType: "image/jpeg"
      })
      .promise()

      return true
  }

}

function createDynamoDBClient() {

  return new AWS.DynamoDB.DocumentClient()

}

