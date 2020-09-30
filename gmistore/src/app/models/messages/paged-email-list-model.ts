import {EmailTableModel} from "./email-table-model";

export interface PagedEmailListModel {
  emails: Array<EmailTableModel>;
  totalElements: number;
}
