import {PageInfo} from './PageInfo';

export interface ApiResponse<T> {
  status: string;
  message: string;
  payload: {
    content: T[];
    page: PageInfo;
  }
}
