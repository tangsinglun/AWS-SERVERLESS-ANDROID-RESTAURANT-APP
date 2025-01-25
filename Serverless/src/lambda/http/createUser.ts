import 'source-map-support/register'

import { APIGatewayProxyEvent, APIGatewayProxyResult, APIGatewayProxyHandler } from 'aws-lambda'
import { checkPhone, createUser } from '../../businessLogic/EatRestaurant';
import { createLogger } from '../../utils/logger'
import { UserItem } from '../../models/UserItem'
import { CreateUserRequest } from '../../requests/CreateUserRequest'

const logger = createLogger('getUsers')

export const handler: APIGatewayProxyHandler = async (event: APIGatewayProxyEvent): Promise<APIGatewayProxyResult> => {

  logger.info('Event Processing', {event: event.body})

  const newUser: CreateUserRequest = JSON.parse(event.body)

  //Extract JWT Token From the Authoriztion Header
  const authorization = event.headers.Authorization
  const split = authorization.split(' ')
  const jwtToken = split[1] 

  logger.info('JWT Token', {jwtToken: jwtToken})
  
  //Check If User Exits
  const isUser: Boolean = await checkPhone(newUser.phone) 
  let items: UserItem = null;

  logger.info('Is User', {isUser: isUser})

  if (!isUser) {
      // const userItems: UserItem[] = await getUser(jwtToken) 

      // logger.info("User's UserId", {"UserId": userItems[0].userId})

      const userDetail = {
                            userId: newUser.userId,
                            name: newUser.name,
                            password_hash: newUser.password_hash,
                            phone: newUser.phone,
                            secureCode: newUser.secureCode,
                            isStaff: newUser.isStaff,
                          }

      items = await createUser(userDetail)

      logger.info('User items', {items: items[0]})

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
        items
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
