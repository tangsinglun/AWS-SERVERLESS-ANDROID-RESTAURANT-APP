import 'source-map-support/register'
import { createLogger } from '../../utils/logger'
import { APIGatewayProxyEvent, APIGatewayProxyResult, APIGatewayProxyHandler } from 'aws-lambda'
import { uploadImageToS3, checkUser } from '../../businessLogic/EatRestaurant'

const logger = createLogger('Upload Image To S3')

export const handler: APIGatewayProxyHandler = async (event: APIGatewayProxyEvent): Promise<APIGatewayProxyResult> => {

  logger.info('Event Processing', {event: event.body})

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

      // const jsonBody = JSON.parse(event.body)

      // const imageData = atob(jsonBody.file.data)
      const data = Buffer.from(event.body, 'base64')
      // const data = JSON.parse(b.toString());

      // const data = event.body

      // logger.info('Image data', {data})

      // Return a presigned URL to upload a file for a Food item with the provided id
      // const url: string = getUploadUrl(foodId)

      logger.info('Begin Upload To S3', {info: "Begin Upload"})

      status = await uploadImageToS3(foodId, data)

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
