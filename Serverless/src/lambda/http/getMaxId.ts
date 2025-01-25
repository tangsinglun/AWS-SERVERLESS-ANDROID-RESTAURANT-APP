import 'source-map-support/register'

import { APIGatewayProxyEvent, APIGatewayProxyResult, APIGatewayProxyHandler } from 'aws-lambda'
import { checkUser, getMaxId } from '../../businessLogic/EatRestaurant';
import { createLogger } from '../../utils/logger'
import { FoodItem } from '../../models/FoodItem'

const logger = createLogger('get maximum id')

export const handler: APIGatewayProxyHandler = async (event: APIGatewayProxyEvent): Promise<APIGatewayProxyResult> => {

  // logger.info('Event Processing', {event: event.body})

  //Extract JWT Token From the Authoriztion Header
  const authorization = event.headers.Authorization
  const split = authorization.split(' ')
  const jwtToken = split[1] 

  logger.info('JWT Token', {jwtToken: jwtToken})
  
  const isUser: Boolean = await checkUser(jwtToken) 
  let items: FoodItem[]

  logger.info('Is User', {isUser: isUser})

  if (isUser) {

      items = await getMaxId()

      logger.info('Maximun items', {items: items[0]})

       // Return the all the User's Item Result back to the Client
      return {
        statusCode: 200,
        headers: {
          'Access-Control-Allow-Origin': '*',
          'Access-Control-Allow-Credentials': true,
          'Access-Control-Allow-Headers': 'Content-Type,X-Amz-Date,Authorization,X-Api-Key,X-Amz-Security-Token',
          'Access-Control-Allow-Methods': 'GET,OPTIONS,POST',
        },
        body: JSON.stringify(
        {items}
        )
      }
  }

  // Return the all the User's Item Result back to the Client
  return {
    statusCode: 200,
    headers: {
      'Access-Control-Allow-Origin': '*',
      'Access-Control-Allow-Credentials': true,
      'Access-Control-Allow-Headers': 'Content-Type,X-Amz-Date,Authorization,X-Api-Key,X-Amz-Security-Token',
      'Access-Control-Allow-Methods': 'GET,OPTIONS,POST',
    },
    body: null
  }

}
