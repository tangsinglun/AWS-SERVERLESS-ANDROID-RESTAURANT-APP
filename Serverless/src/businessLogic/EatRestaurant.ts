import { UserItem } from '../models/UserItem'
import { FoodItem } from '../models/FoodItem'
import { EatRestaurantAccess } from '../dataLayer/eatRestaurantAccess'
import { parseUser } from '../auth/utils'
import { createLogger } from '../utils/logger'

const logger = createLogger('Eat Restaurant Business Logic')

const eatRestaurantAccess = new EatRestaurantAccess()

export async function checkUser(jwtToken): Promise<Boolean> {
  //Extract the UserId From the jwt Token
  const user: UserItem = JSON.parse(parseUser(jwtToken))

  logger.info('checkUser', {user})

  // logger.info('checkUser PHONE', {phone: user.phone})

  // logger.info('checkUser PASSWORD_HASH', {password_hash: user.password_hash})

  //Return all the User's Todo Results 
  return await eatRestaurantAccess.checkUser(user.phone, user.password_hash)
}


export async function checkPhone(phone: string): Promise<Boolean> {
  return await eatRestaurantAccess.checkPhone(phone)
}

export async function getUser(jwtToken): Promise<UserItem[]> {
  //Extract the UserId From the jwt Token
  const user: UserItem = JSON.parse(parseUser(jwtToken))

  // logger.info('getUser Sub', {user})

  //Return all the User's Todo Results 
  return await eatRestaurantAccess.getUser(user.phone, user.password_hash)
}

export async function getFoodItems(foodId: String): Promise<FoodItem[]> {
  // logger.info('getUser Sub', {user})

  //Return all the User's Todo Results 
  return await eatRestaurantAccess.getFoodItems(foodId)
}

export async function getMaxId(): Promise<FoodItem[]> {
  // logger.info('getUser Sub', {user})

  //Return all the User's Todo Results 
  return await eatRestaurantAccess.getMaxId()
}

export async function createFood(
  food: FoodItem
): Promise<FoodItem> {
  return await eatRestaurantAccess.createFood(food)
}

export async function createUser(
  User: UserItem
): Promise<UserItem> {
  return await eatRestaurantAccess.createUser(User)
}


export async function updateFood(
  food: FoodItem
): Promise<FoodItem> {
  return await eatRestaurantAccess.editFood(food)
}

export function getUploadUrl(todoId: string): string {

  //Return the pre-signed Upload URL
  return eatRestaurantAccess.getUploadUrl(todoId)

}

export async function uploadImageToS3(key: string, imageBitMap: any): Promise<boolean> {

  //Return the pre-signed Upload URL
  logger.info('Ready Upload To S3', {info: "Ready Upload"})
  return await eatRestaurantAccess.uploadImageToS3(key, imageBitMap)
}

export async function deleteFood(key: string): Promise<boolean> {

  //Return the pre-signed Upload URL
  logger.info('Ready Delete Food', {info: "Ready Delete"})
  return await eatRestaurantAccess.deleteFood(key)
}