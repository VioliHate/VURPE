export type FieldType = 'string' | 'number' | 'date' | 'uuid';

export interface Field {
  id: string;
  label: string;
  type: FieldType;
}

export type Operator =
  | 'equals'
  | 'not_equals'
  | 'contains'
  | 'starts_with'
  | 'ends_with'
  | 'gt'
  | 'lt'
  | 'gte'
  | 'lte'
  | 'before'
  | 'after'
  | 'on'
  | 'is_null'
  | 'is_not_null';

export interface Condition {
  id: string;
  fieldId: string;
  operator: Operator;
  value: string;
}

export interface RuleGroup {
  id: string;
  conjunction: 'AND' | 'OR';
  conditions: (Condition | RuleGroup)[];
}

export enum RiskFlag {
  LOW = 'LOW',
  MEDIUM = 'MEDIUM',
  HIGH = 'HIGH',
}
