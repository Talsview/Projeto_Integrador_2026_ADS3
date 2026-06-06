export interface MenuItem {
  label: string;
  route: string;
  icon?: string;
  description?: string;
}

export interface MenuGroup {
  title: string;
  items: MenuItem[];
}
