export type RiskFlag = 'HIGH' | 'MEDIUM' | 'LOW';

export interface BusinessRule {
  id: string;
  ruleName: string;
  ruleCondition: string;
  riskFlag: RiskFlag;
  severity: number;
}

export type LogicalOperator = 'AND' | 'OR';

export type Operator = 
  | '=' 
  | '>' 
  | '<' 
  | '>=' 
  | '<=' 
  | '!=' 
  | 'LIKE' 
  | 'IS NULL' 
  | 'IS NOT NULL' 
  | 'BETWEEN'
  | 'LENGTH <'
  | 'LENGTH >';

export interface QueryRule {
  id: string;
  type: 'rule';
  field: string;
  operator: Operator;
  value: string;
  value2?: string; // For BETWEEN
}

export interface QueryGroup {
  id: string;
  type: 'group';
  logicalOperator: LogicalOperator;
  children: (QueryRule | QueryGroup)[];
}

export type QueryNode = QueryRule | QueryGroup;

export const FIELDS = [
  { id: 'amount', label: 'Amount', type: 'number' },
  { id: 'category', label: 'Category', type: 'string' },
  { id: 'description', label: 'Description', type: 'string' },
];

export const OPERATORS: Record<string, { label: string; value: Operator; needsValue: boolean; needsSecondValue?: boolean }> = {
  EQUALS: { label: '=', value: '=', needsValue: true },
  GREATER: { label: '>', value: '>', needsValue: true },
  LESS: { label: '<', value: '<', needsValue: true },
  GREATER_EQUAL: { label: '>=', value: '>=', needsValue: true },
  LESS_EQUAL: { label: '<=', value: '<=', needsValue: true },
  NOT_EQUALS: { label: '!=', value: '!=', needsValue: true },
  LIKE: { label: 'LIKE', value: 'LIKE', needsValue: true },
  IS_NULL: { label: 'IS NULL', value: 'IS NULL', needsValue: false },
  IS_NOT_NULL: { label: 'IS NOT NULL', value: 'IS NOT NULL', needsValue: false },
  BETWEEN: { label: 'BETWEEN', value: 'BETWEEN', needsValue: true, needsSecondValue: true },
  LENGTH_LT: { label: 'Length <', value: 'LENGTH <', needsValue: true },
  LENGTH_GT: { label: 'Length >', value: 'LENGTH >', needsValue: true },
};
