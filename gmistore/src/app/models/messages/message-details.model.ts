export interface MessageDetailsModel {
  id: number;
  sender: string;
  receiver: string;
  subject: string;
  content: string;
  read: boolean;
  timestamp: Date;
}
