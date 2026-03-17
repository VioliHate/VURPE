export class BusinessRule {
  private id: any;
  private ruleName: string;
  private ruleCondition: string;
  private riskFlag: string;
  private severity: number;

  constructor(name: string, condition: string, flag: string, severity: number) {
    this.id = null;
    this.ruleName = name;
    this.ruleCondition = condition;
    this.riskFlag = flag;
    this.severity = severity;
  }
}
