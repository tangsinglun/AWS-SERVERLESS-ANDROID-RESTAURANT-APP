/**
 * Fields in a request to create a single TODO item.
 */
export interface EditFoodRequest {
  id: string
  name: string
  description: string
  price: number
  discount: number
  menuid: string
  image: string
}
