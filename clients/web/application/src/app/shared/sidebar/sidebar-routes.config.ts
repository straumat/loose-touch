import { RouteInfo } from './sidebar.metadata';

export const ROUTES: RouteInfo[] = [

    {
        path: '/full-layout', title: 'Full Layout', icon: 'icon-screen-desktop', class: '', badge: '', badgeClass: '', isExternalLink: false, submenu: []
    },
    {
        path: '/content-layout', title: 'Content Layout', icon: 'icon-grid', class: '', badge: '', badgeClass: '', isExternalLink: false, submenu: []
    },
    {
        path: '', title: 'Menu Levels', icon: 'icon-layers', class: 'has-sub', badge: '1', badgeClass: 'badge badge-pill badge-danger float-right mr-3 mt-1', isExternalLink: false,
        submenu: [
            { path: 'javascript:;', title: 'Second Level', icon: '', class: '', badge: '', badgeClass: '', isExternalLink: true, submenu: [] },
            {
                path: '', title: 'Second Level', icon: '', class: 'has-sub', badge: '', badgeClass: '', isExternalLink: false,
                submenu: [
                    { path: 'javascript:;', title: 'Third Level 1.1', icon: '', class: '', badge: '', badgeClass: '', isExternalLink: true, submenu: [] },
                    { path: 'javascript:;', title: 'Third Level 1.2', icon: '', class: '', badge: '', badgeClass: '', isExternalLink: true, submenu: [] },
                ]
            },
        ]
    },
    {
        path: '/changelog', title: 'ChangeLog', icon: 'icon-doc', class: '', badge: '', badgeClass: '', isExternalLink: false, submenu: []
    },
    { path: 'http://pixinvent.com/demo/convex-angular-bootstrap-admin-dashboard-template/documentation', title: 'Documentation', icon: 'icon-book-open', class: '', badge: '', badgeClass: '', isExternalLink: true, submenu: [] }

];
