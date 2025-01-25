import 'source-map-support/register'
import { createLogger } from '../../utils/logger'
import { APIGatewayProxyEvent, APIGatewayProxyResult, APIGatewayProxyHandler } from 'aws-lambda'
import { deleteFood, checkUser } from '../../businessLogic/EatRestaurant'

const logger = createLogger('Delete Food Item')

export const handler: APIGatewayProxyHandler = async (event: APIGatewayProxyEvent): Promise<APIGatewayProxyResult> => {

  // logger.info('Event Processing', {event: event.body})

  //Extract JWT Token From the Authoriztion Header
  const authorization = event.headers.Authorization
  const split = authorization.split(' ')
  const jwtToken = split[1] 
  let isUser: Boolean = false;
  let status: Boolean = false;


  logger.info('JWT Token', {jwtToken: jwtToken})
  
  isUser = await checkUser(jwtToken)

  logger.info('Is User', {isUser: isUser})

  if (isUser) {  
      //Get the FoodId From the Query String
      const foodId = event.pathParameters.foodId

      logger.info('Begin Delete FoodItem', {info: "Begin Delete"})

      status = await deleteFood(foodId)

        // Return the presigned URL Result back to the Client
        return {
          statusCode: 201,
          headers: {
            'Access-Control-Allow-Origin': '*',
            'Access-Control-Allow-Credentials': true,
            'Access-Control-Allow-Headers': 'Content-Type,X-Amz-Date,Authorization,X-Api-Key,X-Amz-Security-Token',
            'Access-Control-Allow-Methods': 'GET,OPTIONS,POST',
          },    
          body: JSON.stringify({
            status: status
          })
        }
  }
  
  // Return the presigned URL Result back to the Client
  return {
    statusCode: 201,
    headers: {
      'Access-Control-Allow-Origin': '*',
      'Access-Control-Allow-Credentials': true,
      'Access-Control-Allow-Headers': 'Content-Type,X-Amz-Date,Authorization,X-Api-Key,X-Amz-Security-Token',
      'Access-Control-Allow-Methods': 'GET,OPTIONS,POST',
    },    
    body: null
  }

}
