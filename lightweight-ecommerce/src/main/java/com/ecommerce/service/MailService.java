package com.ecommerce.service;

import com.ecommerce.config.AppProperties;
import com.ecommerce.domain.Order;
import com.ecommerce.domain.OrderStatus;
import com.ecommerce.domain.Shipping;
import com.ecommerce.domain.ShippingStatus;
import com.ecommerce.domain.Subscriber;
import com.ecommerce.domain.enums.BannerSlot;
import com.ecommerce.dto.AssetResponse;
import com.ecommerce.dto.OrderEmailDTO;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class MailService {

    private final JavaMailSender    mailSender;
    private final TemplateEngine    templateEngine;
    private final AppProperties     appProperties;
    private final MediaService      mediaService;

    // =========================================================
    // ORDER CONFIRMATION
    // =========================================================

    @Async("taskExecutor")
    public void sendOrderConfirmation(OrderEmailDTO emailDto) {
        if (!isMailEnabled("order confirmation")) return;

        try {
            Context context = buildOrderContext(emailDto);
            String htmlContent = templateEngine.process("order-confirmation", context);

            sendEmail(
                emailDto.getEmail(),
                "Commande confirmée - " + emailDto.getOrderNumber(),
                htmlContent
            );

            log.info("Order confirmation email sent to: {}", emailDto.getEmail());

        } catch (Exception e) {
            log.error("Failed to send order confirmation email to: {}",
                emailDto.getEmail(), e);
        }
    }

    // =========================================================
    // INTERNAL ORDER NOTIFICATION (to store owner)
    // =========================================================

    @Async("taskExecutor")
    public void sendInternalOrderNotification(OrderEmailDTO emailDto) {
        if (!isMailEnabled("internal order notification")) return;

        try {
            String htmlContent = buildInternalOrderNotificationContent(emailDto);

            sendEmail(
                appProperties.getMail().getFromEmail(),
                "🛒 Nouvelle commande - " + emailDto.getOrderNumber(),
                htmlContent
            );

            log.info("Internal order notification sent for order: {}", emailDto.getOrderNumber());

        } catch (Exception e) {
            log.error("Failed to send internal order notification for order: {}",
                emailDto.getOrderNumber(), e);
        }
    }

    private String buildInternalOrderNotificationContent(OrderEmailDTO emailDto) {
        StringBuilder items = new StringBuilder();
        if (emailDto.getOrderItems() != null) {
            for (OrderEmailDTO.OrderItemEmailDTO item : emailDto.getOrderItems()) {
                items.append("""
                    <tr>
                        <td style="padding: 8px 12px; border-bottom: 1px solid #eee;">%s</td>
                        <td style="padding: 8px 12px; border-bottom: 1px solid #eee; text-align:center;">%d</td>
                        <td style="padding: 8px 12px; border-bottom: 1px solid #eee; text-align:right;">%s DT</td>
                    </tr>
                    """.formatted(
                        item.getProductName(),
                        item.getQuantity(),
                        item.getPrice()
                    ));
            }
        }

        return """
            <html>
            <body style="font-family: Arial, sans-serif; padding: 32px 20px;
                        background-color: #f9f9f9; color: #000;">

                <div style="max-width: 580px; margin: 0 auto;
                            background: #fff; padding: 32px;
                            border: 1px solid #e0e0e0;">

                    <!-- Header -->
                    <div style="background-color: #000; padding: 16px 24px;
                                margin: -32px -32px 32px -32px;">
                        <h2 style="color: #FFD700; margin: 0; font-size: 20px;">
                            🛒 Nouvelle Commande Reçue
                        </h2>
                    </div>

                    <!-- Customer Info -->
                    <h3 style="font-size: 15px; font-weight: 700;
                            text-transform: uppercase; letter-spacing: 1px;
                            border-bottom: 2px solid #FFD700;
                            padding-bottom: 8px; margin-bottom: 16px;">
                        Informations Client
                    </h3>

                    <table style="width: 100%%; border-collapse: collapse;
                                margin-bottom: 28px; font-size: 14px;">
                        <tr>
                            <td style="padding: 6px 0; color: #555; width: 40%%;">Nom</td>
                            <td style="padding: 6px 0; font-weight: 600;">%s</td>
                        </tr>
                        <tr>
                            <td style="padding: 6px 0; color: #555;">Email</td>
                            <td style="padding: 6px 0; font-weight: 600;">%s</td>
                        </tr>
                        <tr>
                            <td style="padding: 6px 0; color: #555;">Adresse</td>
                            <td style="padding: 6px 0; font-weight: 600;">%s</td>
                        </tr>
                    </table>

                    <!-- Order Info -->
                    <h3 style="font-size: 15px; font-weight: 700;
                            text-transform: uppercase; letter-spacing: 1px;
                            border-bottom: 2px solid #FFD700;
                            padding-bottom: 8px; margin-bottom: 16px;">
                        Détails Commande
                    </h3>

                    <table style="width: 100%%; border-collapse: collapse;
                                margin-bottom: 28px; font-size: 14px;">
                        <tr>
                            <td style="padding: 6px 0; color: #555; width: 40%%;">Numéro</td>
                            <td style="padding: 6px 0; font-weight: 600;">%s</td>
                        </tr>
                        <tr>
                            <td style="padding: 6px 0; color: #555;">Date</td>
                            <td style="padding: 6px 0; font-weight: 600;">%s</td>
                        </tr>
                    </table>

                    <!-- Items -->
                    <h3 style="font-size: 15px; font-weight: 700;
                            text-transform: uppercase; letter-spacing: 1px;
                            border-bottom: 2px solid #FFD700;
                            padding-bottom: 8px; margin-bottom: 16px;">
                        Articles
                    </h3>

                    <table style="width: 100%%; border-collapse: collapse;
                                font-size: 14px; margin-bottom: 28px;">
                        <thead>
                            <tr style="background-color: #f5f5f5;">
                                <th style="padding: 8px 12px; text-align:left;">Produit</th>
                                <th style="padding: 8px 12px; text-align:center;">Qté</th>
                                <th style="padding: 8px 12px; text-align:right;">Prix</th>
                            </tr>
                        </thead>
                        <tbody>
                            %s
                        </tbody>
                    </table>

                    <!-- Totals -->
                    <table style="width: 100%%; border-collapse: collapse;
                                font-size: 14px; margin-bottom: 8px;">
                        <tr>
                            <td style="padding: 4px 0; color: #555;">Sous-total</td>
                            <td style="padding: 4px 0; text-align:right;
                                    font-weight: 600;">%s DT</td>
                        </tr>
                        <tr>
                            <td style="padding: 4px 0; color: #555;">Livraison</td>
                            <td style="padding: 4px 0; text-align:right;
                                    font-weight: 600;">%s DT</td>
                        </tr>
                        <tr>
                            <td colspan="2">
                                <div style="height:1px; background:#000;
                                            margin: 8px 0;"></div>
                            </td>
                        </tr>
                        <tr>
                            <td style="font-weight: 700; font-size: 16px;">Total</td>
                            <td style="font-weight: 700; font-size: 18px;
                                    text-align:right; color: #E74C3C;">
                                %s DT
                            </td>
                        </tr>
                    </table>

                </div>
            </body>
            </html>
            """.formatted(
                emailDto.getName(),
                emailDto.getEmail() != null ? emailDto.getEmail() : "—",
                emailDto.getDeliveryAddress() != null ? emailDto.getDeliveryAddress() : "—",
                emailDto.getOrderNumber(),
                emailDto.getOrderDate(),
                items.toString(),
                emailDto.getSubtotal(),
                emailDto.getShipping(),
                emailDto.getTotal()
            );
    }

    // =========================================================
    // SUBSCRIPTION
    // =========================================================

    @Async("taskExecutor")
    public void sendSubscriptionConfirmation(Subscriber subscriber, String discountCode) {
        if (!isMailEnabled("subscription confirmation")) return;

        try {
            Context context = buildSubscriptionContext(subscriber, discountCode);
            String htmlContent = templateEngine.process("subscription-confirmation", context);

            sendEmail(
                subscriber.getEmail(),
                "Bienvenue chez " + appProperties.getCompanyName() + " !",
                htmlContent
            );

            log.info("Subscription confirmation email sent to: {}", subscriber.getEmail());

        } catch (Exception e) {
            log.error("Failed to send subscription email to: {}",
                subscriber.getEmail(), e);
        }
    }

    private Context buildSubscriptionContext(Subscriber subscriber, String discountCode) {
        Context context = new Context();

        context.setVariable("name",         subscriber.getName());
        context.setVariable("email",        subscriber.getEmail());
        context.setVariable("brandName",    appProperties.getCompanyName());
        context.setVariable("supportEmail", appProperties.getSupportEmail());
        context.setVariable("websiteUrl",   appProperties.getFrontendUrl());
        context.setVariable("unsubscribeUrl",
            appProperties.getFrontendUrl() + "/unsubscribe/" + subscriber.getEmail()
        );

        context.setVariable("discountCode", discountCode);
        context.setVariable("hasDiscount",  discountCode != null && !discountCode.isBlank());

        context.setVariable("headerLogoUrl", resolveLogoUrl(BannerSlot.MAIL_WELCOME_HEADER_LOGO));
        context.setVariable("footerLogoUrl", resolveLogoUrl(BannerSlot.MAIL_FOOTER_LOGO));

        return context;
    }

    // =========================================================
    // ORDER STATUS UPDATE
    // =========================================================

    @Async("taskExecutor")
    public void sendOrderStatusUpdate(Order order, OrderStatus previousStatus) {
        if (!isMailEnabled("order status update")) return;

        try {
            Context context = new Context();
            context.setVariable("customerName",   order.getCustomerName());
            context.setVariable("orderNumber",    order.getOrderNumber());
            context.setVariable("newStatus",      order.getStatus().name());
            context.setVariable("previousStatus", previousStatus.name());
            injectSharedMailVariables(context);

            String htmlContent = templateEngine.process("order-status-update", context);

            sendEmail(
                order.getCustomerEmail(),
                "Mise à jour de commande - " + order.getOrderNumber(),
                htmlContent
            );

            log.info("Order status update email sent to: {}", order.getCustomerEmail());

        } catch (Exception e) {
            log.error("Failed to send order status update email to: {}",
                order.getCustomerEmail(), e);
        }
    }

    // =========================================================
    // SHIPPING
    // =========================================================

    @Async("taskExecutor")
    public void sendShippingCreated(Shipping shipping) {
        if (!isMailEnabled("shipping created")) return;

        try {
            Context context = new Context();
            context.setVariable("trackingNumber",    shipping.getTrackingNumber());
            context.setVariable("orderNumber",       shipping.getOrder().getOrderNumber());
            context.setVariable("customerName",      shipping.getOrder().getCustomerName());
            context.setVariable("carrier",           shipping.getCarrier());
            context.setVariable("estimatedDelivery", shipping.getEstimatedDeliveryDate());
            injectSharedMailVariables(context);

            String htmlContent = templateEngine.process("shipping-created", context);

            sendEmail(
                shipping.getOrder().getCustomerEmail(),
                "Expédition confirmée - " + shipping.getTrackingNumber(),
                htmlContent
            );

            log.info("Shipping email sent for: {}", shipping.getTrackingNumber());

        } catch (Exception e) {
            log.error("Failed to send shipping email for: {}",
                shipping.getTrackingNumber(), e);
        }
    }

    @Async("taskExecutor")
    public void sendShippingStatusUpdate(Shipping shipping, ShippingStatus newStatus) {
        if (!isMailEnabled("shipping status update")) return;

        try {
            Context context = new Context();
            context.setVariable("trackingNumber", shipping.getTrackingNumber());
            context.setVariable("orderNumber",    shipping.getOrder().getOrderNumber());
            context.setVariable("customerName",   shipping.getOrder().getCustomerName());
            context.setVariable("status",         newStatus.getStatus().getDisplayName());
            context.setVariable("location",       newStatus.getLocation());
            context.setVariable("timestamp",      newStatus.getTimestamp());
            context.setVariable("description",    newStatus.getDescription());
            injectSharedMailVariables(context);

            String htmlContent = templateEngine.process("shipping-status-update", context);

            sendEmail(
                shipping.getOrder().getCustomerEmail(),
                "Mise à jour livraison - " + shipping.getTrackingNumber(),
                htmlContent
            );

            log.info("Shipping status update email sent for: {}",
                shipping.getTrackingNumber());

        } catch (Exception e) {
            log.error("Failed to send shipping status update email for: {}",
                shipping.getTrackingNumber(), e);
        }
    }

    // =========================================================
    // TEST EMAIL
    // =========================================================

    public MailTestResult sendTestEmail(String to) {
        log.info("Sending test email to: {}", to);

        try {
            String htmlContent = buildTestEmailContent();
            sendEmail(to, "Test Email - Configuration Check", htmlContent);
            log.info("Test email successfully sent to: {}", to);
            return MailTestResult.success(to);

        } catch (MessagingException e) {
            log.error("SMTP error sending test email to: {}", to, e);
            return MailTestResult.failure(to, "SMTP Error: " + e.getMessage());

        } catch (UnsupportedEncodingException e) {
            log.error("Encoding error sending test email to: {}", to, e);
            return MailTestResult.failure(to, "Encoding Error: " + e.getMessage());
        }
    }

    // =========================================================
    // SYNC (for testing)
    // =========================================================

    public void sendOrderConfirmationSync(OrderEmailDTO emailDto)
            throws MessagingException, UnsupportedEncodingException {

        Context context = buildOrderContext(emailDto);
        String htmlContent = templateEngine.process("order-confirmation", context);

        sendEmail(
            emailDto.getEmail(),
            "Commande confirmée - " + emailDto.getOrderNumber(),
            htmlContent
        );

        log.info("Order confirmation email sent (sync) to: {}", emailDto.getEmail());
    }

    // =========================================================
    // PRIVATE — Context builders
    // =========================================================

    private Context buildOrderContext(OrderEmailDTO emailDto) {
        Context context = new Context();

        context.setVariable("name",            emailDto.getName());
        context.setVariable("orderNumber",     emailDto.getOrderNumber());
        context.setVariable("orderDate",       emailDto.getOrderDate());
        context.setVariable("deliveryAddress", emailDto.getDeliveryAddress());
        context.setVariable("orderItems",      emailDto.getOrderItems());
        context.setVariable("subtotal",        emailDto.getSubtotal());
        context.setVariable("shipping",        emailDto.getShipping());
        context.setVariable("total",           emailDto.getTotal());
        context.setVariable("discountCode",    emailDto.getDiscountCode());
        context.setVariable("companyName",     emailDto.getCompanyName());
        context.setVariable("supportEmail",    emailDto.getSupportEmail());
        context.setVariable("frontendUrl",     emailDto.getFrontendUrl());

        context.setVariable("headerLogoUrl",   resolveLogoUrl(BannerSlot.MAIL_ORDER_HEADER_LOGO));
        context.setVariable("footerLogoUrl",   resolveLogoUrl(BannerSlot.MAIL_FOOTER_LOGO));

        return context;
    }

    private Context buildSubscriptionContext(Subscriber subscriber) {
        Context context = new Context();

        context.setVariable("name",         subscriber.getName());
        context.setVariable("email",        subscriber.getEmail());
        context.setVariable("brandName",    appProperties.getCompanyName());
        context.setVariable("supportEmail", appProperties.getSupportEmail());
        context.setVariable("websiteUrl",   appProperties.getFrontendUrl());
        context.setVariable("unsubscribeUrl",
            appProperties.getFrontendUrl() + "/unsubscribe/" + subscriber.getEmail()
        );

        context.setVariable("headerLogoUrl", resolveLogoUrl(BannerSlot.MAIL_WELCOME_HEADER_LOGO));
        context.setVariable("footerLogoUrl", resolveLogoUrl(BannerSlot.MAIL_FOOTER_LOGO));

        return context;
    }

    private void injectSharedMailVariables(Context context) {
        context.setVariable("companyName",   appProperties.getCompanyName());
        context.setVariable("supportEmail",  appProperties.getSupportEmail());
        context.setVariable("frontendUrl",   appProperties.getFrontendUrl());
        context.setVariable("footerLogoUrl", resolveLogoUrl(BannerSlot.MAIL_FOOTER_LOGO));
    }

    // =========================================================
    // PRIVATE — Logo resolution
    // =========================================================

    /**
     * Resolves the primary asset URL for a given banner slot.
     * Always returns a fully absolute public URL for use in emails.
     *
     * Example:
     *   /media/brand/logo.png
     *   → https://www.jawista.com/media/brand/logo.png
     */
    private String resolveLogoUrl(BannerSlot slot) {
        try {
            AssetResponse asset = mediaService.getPrimaryAsset(
                slot.getAssetOwnerType(),
                slot.getOwnerId()
            );

            if (asset == null || asset.getUrl() == null || asset.getUrl().isBlank()) {
                return null;
            }

            return toAbsoluteMediaUrl(asset.getUrl());

        } catch (Exception e) {
            log.warn("[MailService] Could not resolve logo for slot {}: {}",
                slot.name(), e.getMessage());
            return null;
        }
    }

    /**
     * Converts any media URL (relative or absolute) into a
     * fully qualified public URL using FRONTEND_URL as the base.
     *
     * Examples:
     *   "/media/brand/logo.png"
     *     → "https://www.jawista.com/media/brand/logo.png"
     *
     *   "http://localhost:8080/media/brand/logo.png"
     *     → "https://www.jawista.com/media/brand/logo.png"
     */
    private String toAbsoluteMediaUrl(String url) {
        if (url == null || url.isBlank()) return null;

        String frontendUrl = appProperties.getFrontendUrl();
        if (frontendUrl == null || frontendUrl.isBlank()) {
            log.warn("[MailService] FRONTEND_URL is not configured. Returning raw asset URL.");
            return url;
        }

        // Strip trailing slash from base
        String base = frontendUrl.endsWith("/")
            ? frontendUrl.substring(0, frontendUrl.length() - 1)
            : frontendUrl;

        try {
            // If already absolute (any host), extract only the path
            URL parsed = new URL(url);
            String path = parsed.getPath();
            return base + path;
        } catch (MalformedURLException e) {
            // Already a relative path like /media/brand/logo.png
            String path = url.startsWith("/") ? url : "/" + url;
            return base + path;
        }
    }

    // =========================================================
    // PRIVATE — Email sender
    // =========================================================

    private void sendEmail(String to, String subject, String htmlContent)
            throws MessagingException, UnsupportedEncodingException {

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setFrom(
            appProperties.getMail().getFromEmail(),
            appProperties.getMail().getFromName()
        );
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(htmlContent, true);

        mailSender.send(message);
    }

    private boolean isMailEnabled(String emailType) {
        if (!appProperties.getMail().isEnabled()) {
            log.info("Email disabled. Skipping [{}] email.", emailType);
            return false;
        }
        return true;
    }

    private String buildTestEmailContent() {
        String timestamp = LocalDateTime.now().toString();

        return """
            <html>
            <body style="font-family: Arial, sans-serif; padding: 20px;">
                <h2 style="color: #2c3e50;">Email Configuration Test</h2>
                <p>This is a test email from <strong>%s</strong>.</p>
                <hr/>
                <h4>Configuration Details:</h4>
                <ul>
                    <li><strong>From:</strong> %s</li>
                    <li><strong>Support:</strong> %s</li>
                    <li><strong>Timestamp:</strong> %s</li>
                </ul>
                <p style="color: green;">
                    Email configuration is working correctly.
                </p>
            </body>
            </html>
            """.formatted(
                appProperties.getCompanyName(),
                appProperties.getMail().getFromEmail(),
                appProperties.getSupportEmail(),
                timestamp
            );
    }

    // =========================================================
    // RESULT RECORD
    // =========================================================

    public record MailTestResult(
        boolean success,
        String recipient,
        String message,
        LocalDateTime timestamp
    ) {
        public static MailTestResult success(String recipient) {
            return new MailTestResult(true, recipient,
                "Email sent successfully", LocalDateTime.now());
        }

        public static MailTestResult failure(String recipient, String reason) {
            return new MailTestResult(false, recipient,
                reason, LocalDateTime.now());
        }
    }
}