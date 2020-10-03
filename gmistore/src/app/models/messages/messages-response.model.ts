import {MessageDetailsModel} from "./message-details.model";

export interface MessagesResponseModel {
  incoming: Array<MessageDetailsModel>;
  outgoing: Array<MessageDetailsModel>;
}
