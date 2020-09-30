export interface EmailTableModel{
  id: number,
  email: string,
  subject: string,
  message: string,
  creatingTime: Date,
  active: boolean,
}
