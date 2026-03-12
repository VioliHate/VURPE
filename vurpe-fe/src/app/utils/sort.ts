export class SortUrl {
  public sort: string ;
  constructor(sortField:string,sortDir:string){
    this.sort=this.snakeToCamel(sortField)+","+sortDir.toUpperCase()
  }
  private snakeToCamel(str: string): string {
    if (!str) return '';
    return str.replace(/_([a-z0-9])/gi, (match, letter) => letter.toUpperCase());
  }

  public toString(){
    return this.sort.toString();
  }


}
