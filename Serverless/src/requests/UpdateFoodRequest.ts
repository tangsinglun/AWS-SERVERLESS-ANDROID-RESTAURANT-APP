/**
 * Fields in a request to update a single TODO item.
 */
export interface UpdateFoodRequest {
  name: string
  description: string
  price: number
  discount: number
  menuid: string
}