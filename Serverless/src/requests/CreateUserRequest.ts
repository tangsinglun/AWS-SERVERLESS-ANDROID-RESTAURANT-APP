/**
 * Fields in a request to create a single TODO item.
 */
export interface CreateUserRequest {
  userId: string
  name: string
  password_hash: string
  phone: string
  secureCode: string
  isStaff
}
