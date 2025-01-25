import 'source-map-support/register'

import { APIGatewayProxyEvent, APIGatewayProxyHandler, APIGatewayProxyResult } from 'aws-lambda'

import { EditFoodRequest } from '../../requests/EditFoodRequest'
import { FoodItem } from '../../models/FoodItem'
import { checkUser, updateFood } from '../../businessLogic/EatRestaurant'
import { createLogger } from '../../utils/logger'

const logger = createLogger('editFood')

export const handler: APIGatewayProxyHandler = async (event: APIGatewayProxyEvent): Promise<APIGatewayProxyResult> => {

  logger.info('Event Processing', {event: event.body})

  const editFood: EditFoodRequest = JSON.parse(event.body)

  //Extract JWT Token From the Authoriztion Header
  const authorization = event.headers.Authorization
  const split = authorization.split(' ')
  const jwtToken = split[1]
  const bucketName = process.env.EAT_RESTAURANT_S3_BUCKET
  let editFoodItem: FoodItem

  //Get the FoodId From the Query String
  // const foodId = event.pathParameters.foodId

  const isUser: Boolean = await checkUser(jwtToken) 

  if (isUser) {

      //Add Edit Food Item and Return the Result
      editFoodItem = await updateFood({
                                                id: editFood.id,
                                                name: editFood.name,
                                                image: `https://${bucketName}.s3.us-east-2.amazonaws.com/${editFood.image}.jpeg`,
                                                description: editFood.description,
                                                price: editFood.price,
                                                discount: editFood.discount,
                                                menuid: editFood.menuid
                                          })

      logger.info('Edit Item', editFoodItem) 

        // Return the Edit Item Result back to the Client
        return {
          statusCode: 201,
          headers: {
            'Access-Control-Allow-Origin': '*',
            'Access-Control-Allow-Credentials': true
          },
          body: JSON.stringify({
            item: editFoodItem
          })
        }

  }                      

    // Return the Edit Item Result back to the Client
    return {
      statusCode: 201,
      headers: {
        'Access-Control-Allow-Origin': '*',
        'Access-Control-Allow-Credentials': true
      },
      body: null
    }

}
