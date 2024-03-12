enum status {
  OPERATIONAL,
  NON_OPERATIONAL,
}

export interface Item {
    serialNumber: string;
    status:       status.OPERATIONAL;
    productId:    number;
  }
  