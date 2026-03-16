type FieldType = 'string' | 'number' | 'date' | 'uuid';

interface Field {
  id: string;
  label: string;
  type: FieldType;
}

type Operator =
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

interface Condition {
  id: string;
  fieldId: string;
  operator: Operator;
  value: string;
}

interface RuleGroup {
  id: string;
  conjunction: 'AND' | 'OR';
  conditions: (Condition | RuleGroup)[];
}
