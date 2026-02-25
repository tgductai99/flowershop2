/**
 * Custom JavaScript for AccessMotor
 * Handles custom interactions and enhancements
 */

// Initialize on DOM ready
document.addEventListener('DOMContentLoaded', function() {
    // Auto-hide alerts after 3 seconds
    initAutoHideAlerts();
    
    // Sidebar toggle functionality for admin dashboard
    initSidebarToggle();
});

/**
 * Initialize auto-hide functionality for alerts
 */
function initAutoHideAlerts() {
    const alerts = document.querySelectorAll('.alert.auto-hide');
    alerts.forEach(alert => {
        setTimeout(() => {
            // Use Bootstrap's Alert component for smooth fade out
            const bsAlert = bootstrap.Alert.getOrCreateInstance(alert);
            bsAlert.close();
        }, 5000); // 5 seconds
    });
}

/**
 * Initialize sidebar toggle with simple show/hide and content expansion
 */
function initSidebarToggle() {
    console.log('Initializing sidebar toggle...');
    
    const sidebar = document.getElementById('adminSidebar');
    const toggleButton = document.getElementById('sidebarToggleBtn');
    const mainContent = document.getElementById('admin-main-content');
    
    if (!sidebar || !toggleButton || !mainContent) {
        console.log('Sidebar, toggle button, or main content not found - not on admin page');
        return;
    }
    
    console.log('Sidebar, toggle button, and main content found');
    
    // Create backdrop for mobile
    let backdrop = document.querySelector('.sidebar-backdrop');
    if (!backdrop) {
        backdrop = document.createElement('div');
        backdrop.className = 'sidebar-backdrop';
        document.body.appendChild(backdrop);
    }
    
    // Toggle function
    function toggleSidebar() {
        console.log('Toggling sidebar...');
        const isDesktop = window.innerWidth >= 768;
        
        if (isDesktop) {
            // Desktop: toggle transform (slide in/out) and expand content
            sidebar.classList.toggle('hidden');
            const isHidden = sidebar.classList.contains('hidden');
            
            // Add/remove expanded class to main content
            if (isHidden) {
                mainContent.classList.add('expanded');
            } else {
                mainContent.classList.remove('expanded');
            }
            
            console.log('Desktop toggle - hidden class:', isHidden, 'expanded class:', isHidden);
        } else {
            // Mobile: slide in/out with backdrop
            const isVisible = sidebar.classList.contains('show');
            if (isVisible) {
                sidebar.classList.remove('show');
                backdrop.classList.remove('show');
                document.body.style.overflow = '';
            } else {
                sidebar.classList.add('show');
                backdrop.classList.add('show');
                document.body.style.overflow = 'hidden';
            }
            console.log('Mobile toggle - show class:', sidebar.classList.contains('show'));
        }
    }
    
    // Add click event listener ONLY to the toggle button
    toggleButton.addEventListener('click', function(e) {
        e.preventDefault();
        e.stopPropagation(); // Prevent event bubbling
        console.log('Toggle button clicked!');
        toggleSidebar();
    });
    
    // Prevent sidebar links from triggering toggle
    const sidebarLinks = sidebar.querySelectorAll('.nav-link');
    sidebarLinks.forEach(link => {
        link.addEventListener('click', function(e) {
            // Allow normal navigation - don't prevent default
            console.log('Sidebar link clicked:', this.textContent.trim());
            // On mobile, close sidebar after clicking a link
            if (window.innerWidth < 768) {
                sidebar.classList.remove('show');
                backdrop.classList.remove('show');
                document.body.style.overflow = '';
            }
        });
    });
    
    // Close sidebar when clicking backdrop (mobile)
    backdrop.addEventListener('click', function() {
        console.log('Backdrop clicked');
        sidebar.classList.remove('show');
        backdrop.classList.remove('show');
        document.body.style.overflow = '';
    });
    
    // Handle window resize
    window.addEventListener('resize', function() {
        const isDesktop = window.innerWidth >= 768;
        if (isDesktop) {
            // Desktop: remove mobile classes
            sidebar.classList.remove('show');
            backdrop.classList.remove('show');
            document.body.style.overflow = '';
        } else {
            // Mobile: remove desktop classes and expanded state
            sidebar.classList.remove('hidden');
            mainContent.classList.remove('expanded');
        }
    });
    
    console.log('Sidebar toggle initialized successfully');
}

