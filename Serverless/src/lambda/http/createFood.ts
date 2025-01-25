import 'source-map-support/register'

import { APIGatewayProxyEvent, APIGatewayProxyHandler, APIGatewayProxyResult } from 'aws-lambda'

import { CreateFoodRequest } from '../../requests/CreateFoodRequest'
import { FoodItem } from '../../models/FoodItem'
import { checkUser, createFood } from '../../businessLogic/EatRestaurant'
import { createLogger } from '../../utils/logger'

const logger = createLogger('createFood')

export const handler: APIGatewayProxyHandler = async (event: APIGatewayProxyEvent): Promise<APIGatewayProxyResult> => {

  logger.info('Event Processing', {event: event.body})

  const newFood: CreateFoodRequest = JSON.parse(event.body)

  //Extract JWT Token From the Authoriztion Header
  const authorization = event.headers.Authorization
  const split = authorization.split(' ')
  const jwtToken = split[1]
  const bucketName = process.env.EAT_RESTAURANT_S3_BUCKET
  let newFoodItem: FoodItem

  //Get the FoodId From the Query String
  // const foodId = event.pathParameters.foodId

  const isUser: Boolean = await checkUser(jwtToken) 

  if (isUser) {

      //Add New Food Item and Return the Result
      newFoodItem = await createFood({
                                                id: newFood.id,
                                                name: newFood.name,
                                                image: `https://${bucketName}.s3.us-east-2.amazonaws.com/${newFood.image}.jpeg`,
                                                description: newFood.description,
                                                price: newFood.price,
                                                discount: newFood.discount,
                                                menuid: newFood.menuid
                                          })

      logger.info('New Item', newFoodItem) 

        // Return the New Item Result back to the Client
        return {
          statusCode: 201,
          headers: {
            'Access-Control-Allow-Origin': '*',
            'Access-Control-Allow-Credentials': true
          },
          body: JSON.stringify({
            item: newFoodItem
          })
        }
  }                      

    // Return the New Item Result back to the Client
    return {
      statusCode: 201,
      headers: {
        'Access-Control-Allow-Origin': '*',
        'Access-Control-Allow-Credentials': true
      },
      body: null
    }

}
