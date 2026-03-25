export class BusinessRule {
  private ruleName: string;
  private ruleCondition: string;
  private riskFlag: string;
  private severity: number;

  constructor(name: string, condition: string, flag: string, severity: number) {
    this.ruleName = name;
    this.ruleCondition = condition;
    this.riskFlag = flag;
    this.severity = severity;
  }
}
