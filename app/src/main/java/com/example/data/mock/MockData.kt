package com.example.data.mock

import com.example.data.*

object MockData {
    val initialLeads = listOf(
        LeadEntity(
            id = "lead_001",
            customer = "Jane Doe",
            initials = "JD",
            channel = "whatsapp",
            status = "new",
            source = "Inbound Website",
            receivedAt = "2025-05-24T10:42:00Z",
            summary = "Interested in enterprise pricing for a team of 50. Asked about onboarding timelines.",
            sopMatch = "Enterprise Inquiry (50+ users) → Propose Enterprise Tier & Schedule Onboarding Call",
            aiSummary = "Lead is evaluating enterprise CRM for a 50-person team. Primary concerns are onboarding speed and pricing. Demo booked for tomorrow 2 PM EST.",
            messagesStr = """
                [
                  {"id": "m1", "role": "customer", "content": "I saw your pricing page but I have a few questions about the enterprise tier and onboarding timelines.", "timestamp": "2025-05-24T10:42:00Z"},
                  {"id": "m2", "role": "agent", "content": "Hi Jane! Happy to help. Our Enterprise plan includes dedicated onboarding with a success manager. For 50 seats, we typically complete setup in under 2 weeks. Want me to book a call?", "timestamp": "2025-05-24T10:45:00Z"},
                  {"id": "m3", "role": "customer", "content": "That sounds great. Tomorrow at 2 PM EST works for me.", "timestamp": "2025-05-24T11:15:00Z"}
                ]
            """.trimIndent(),
            timelineStr = """
                [
                  {"step": "Received", "time": "10:42 AM", "done": true},
                  {"step": "Qualified", "time": "10:45 AM", "done": true},
                  {"step": "Escalated", "time": "Pending Demo", "done": false, "active": true},
                  {"step": "Resolved", "time": null, "done": false}
                ]
            """.trimIndent()
        ),
        LeadEntity(
            id = "lead_002",
            customer = "Michael Smith",
            initials = "MS",
            channel = "call",
            status = "qualified",
            source = "Referral",
            receivedAt = "2025-05-24T09:30:00Z",
            summary = "Following up on contract from yesterday's call. Needs final approval.",
            sopMatch = "Contract Follow-up → Send Contract & Flag for Legal Review",
            aiSummary = "Returning customer following up on contract from previous call. High intent, just needs paperwork.",
            messagesStr = """
                [
                  {"id": "m1", "role": "customer", "content": "Following up on our call yesterday, can you send over the contract?", "timestamp": "2025-05-24T09:30:00Z"},
                  {"id": "m2", "role": "agent", "content": "Of course Michael! I'll send it over to your email right now. You should receive it within the next 5 minutes.", "timestamp": "2025-05-24T09:32:00Z"}
                ]
            """.trimIndent(),
            timelineStr = """
                [
                  {"step": "Received", "time": "9:30 AM", "done": true},
                  {"step": "Qualified", "time": "9:32 AM", "done": true},
                  {"step": "Escalated", "time": null, "done": false, "active": false},
                  {"step": "Resolved", "time": null, "done": false}
                ]
            """.trimIndent()
        ),
        LeadEntity(
            id = "lead_003",
            customer = "Alex Kumar",
            initials = "AK",
            channel = "email",
            status = "escalated",
            source = "Cold Outbound",
            receivedAt = "2025-05-23T16:00:00Z",
            summary = "Reporting a 500 error on the portal. Access blocked, needs urgent fix.",
            sopMatch = "Technical Escalation (Portal Access) → Escalate to Engineering + Notify Account Manager",
            aiSummary = "Customer experiencing critical portal access failure. Team blocked. SLA risk — engineering escalated, account manager loop required.",
            messagesStr = """
                [
                  {"id": "m1", "role": "customer", "content": "I am having trouble accessing the portal, it keeps returning a 500 error. This is blocking my entire team.", "timestamp": "2025-05-23T16:00:00Z"},
                  {"id": "m2", "role": "agent", "content": "Hi Alex, I'm escalating this to our technical team immediately. You'll hear from them within 30 minutes.", "timestamp": "2025-05-23T16:05:00Z"},
                  {"id": "m3", "role": "customer", "content": "Still not fixed. This is unacceptable — we have a deadline in 2 hours.", "timestamp": "2025-05-23T17:00:00Z"}
                ]
            """.trimIndent(),
            timelineStr = """
                [
                  {"step": "Received", "time": "4:00 PM", "done": true},
                  {"step": "Qualified", "time": "4:05 PM", "done": true},
                  {"step": "Escalated", "time": "4:06 PM", "done": true, "active": true},
                  {"step": "Resolved", "time": null, "done": false}
                ]
            """.trimIndent()
        ),
        LeadEntity(
            id = "lead_004",
            customer = "Sarah Parker",
            initials = "SP",
            channel = "email",
            status = "new",
            source = "Website Lead",
            receivedAt = "2025-05-24T11:50:00Z",
            summary = "Interested in learning more about enterprise pricing for a team of 50.",
            sopMatch = "Enterprise Inquiry → Route to Sales",
            aiSummary = "New inbound enterprise lead. Early stage, needs qualification call.",
            messagesStr = """
                [
                  {"id": "m1", "role": "customer", "content": "Hi, I'm interested in learning more about your enterprise pricing plans for a team of 50.", "timestamp": "2025-05-24T11:50:00Z"}
                ]
            """.trimIndent(),
            timelineStr = """
                [
                  {"step": "Received", "time": "11:50 AM", "done": true},
                  {"step": "Qualified", "time": null, "done": false, "active": true},
                  {"step": "Escalated", "time": null, "done": false},
                  {"step": "Resolved", "time": null, "done": false}
                ]
            """.trimIndent()
        ),
        LeadEntity(
            id = "lead_005",
            customer = "Mike Kowalski",
            initials = "MK",
            channel = "whatsapp",
            status = "escalated",
            source = "Referral",
            receivedAt = "2025-05-24T09:14:00Z",
            summary = "Contract revision delayed. Customer is frustrated and requesting manager.",
            sopMatch = "Pricing Complaint → Escalate to Manager + Expedite Review",
            aiSummary = "Customer unhappy with delayed contract revision. Escalated to manager. High churn risk if not resolved today.",
            messagesStr = """
                [
                  {"id": "m1", "role": "customer", "content": "Still waiting on the contract revision we am discussed yesterday. Can we expedite?", "timestamp": "2025-05-24T09:14:00Z"},
                  {"id": "m2", "role": "agent", "content": "Mike, I sincerely apologize for the delay. I'm escalating this to our contracts manager right now.", "timestamp": "2025-05-24T09:20:00Z"}
                ]
            """.trimIndent(),
            timelineStr = """
                [
                  {"step": "Received", "time": "9:14 AM", "done": true},
                  {"step": "Qualified", "time": "9:20 AM", "done": true},
                  {"step": "Escalated", "time": "9:21 AM", "done": true, "active": true},
                  {"step": "Resolved", "time": null, "done": false}
                ]
            """.trimIndent()
        ),
        LeadEntity(
            id = "esc_001",
            customer = "Sarah Jenkins",
            initials = "SJ",
            channel = "call",
            status = "escalated",
            source = "Inbound Support",
            receivedAt = "2025-05-24T11:40:00Z",
            summary = "Delayed onboarding process. Customer is very frustrated and requesting an immediate manager callback.",
            sopMatch = "Onboarding Delay > 5 days → Escalate to Customer Success Lead",
            aiSummary = "Customer onboarding has been stalled for over 5 days. Critical cancellation warning. Requires immediate callback by Customer Success leader.",
            messagesStr = """
                [
                  {"id": "m1", "role": "customer", "content": "I was promised our onboarding would be finished by Monday. It is now Friday and we still don't have access!", "timestamp": "2025-05-24T11:40:00Z"},
                  {"id": "m2", "role": "agent", "content": "Hello Sarah, we sincerely apologize. Let me raise this immediately with our success lead.", "timestamp": "2025-05-24T11:45:00Z"}
                ]
            """.trimIndent(),
            timelineStr = """
                [
                  {"step": "Received", "time": "11:40 AM", "done": true},
                  {"step": "Qualified", "time": "11:45 AM", "done": true},
                  {"step": "Escalated", "time": "11:46 AM", "done": true, "active": true},
                  {"step": "Resolved", "time": null, "done": false}
                ]
            """.trimIndent()
        ),
        LeadEntity(
            id = "esc_002",
            customer = "TechCorp Inc.",
            initials = "TC",
            channel = "email",
            status = "escalated",
            source = "Billing Portal",
            receivedAt = "2025-05-24T09:50:00Z",
            summary = "Billing discrepancy on the latest invoice. Charged for an extra user license.",
            sopMatch = "Billing Error → Raise Ticket to Finance + Issue Credit Note",
            aiSummary = "Invoice mismatch detected by client. Charged for 51 users instead of 50. Finance ticket opened, credit note is pending approval.",
            messagesStr = """
                [
                  {"id": "m1", "role": "customer", "content": "Our team has exactly 50 members but we were charged for 51 user seats this month.", "timestamp": "2025-05-24T09:50:00Z"},
                  {"id": "m2", "role": "agent", "content": "Checking details... Yes, you are right. Reverting with a refund receipt and credit note shortly.", "timestamp": "2025-05-24T09:55:00Z"}
                ]
            """.trimIndent(),
            timelineStr = """
                [
                  {"step": "Received", "time": "9:50 AM", "done": true},
                  {"step": "Qualified", "time": "9:55 AM", "done": true},
                  {"step": "Escalated", "time": "9:56 AM", "done": true, "active": true},
                  {"step": "Resolved", "time": null, "done": false}
                ]
            """.trimIndent()
        ),
        LeadEntity(
            id = "esc_003",
            customer = "Global Logistics",
            initials = "GL",
            channel = "whatsapp",
            status = "escalated",
            source = "API Gateway Monitor",
            receivedAt = "2025-05-14T11:05:00Z",
            summary = "API integration failing with 502 Bad Gateway for 3+ hours, blocking their core order system.",
            sopMatch = "Enterprise SLA Breach → Escalate to Engineering + Account Director",
            aiSummary = "Critical webhook delivery failing. Enterprise SLA breached (3+ hours outage). Engineering has identified a hotfix; Account Director is coordinating client relations.",
            messagesStr = """
                [
                  {"id": "m1", "role": "customer", "content": "Your webhook is returning 502 Bad Gateway on our end! Our entire courier fleet dispatch is frozen.", "timestamp": "2025-05-14T11:05:00Z"},
                  {"id": "m2", "role": "agent", "content": "We have alerted our infrastructure engineering team. They are deploying a service roll-back immediately.", "timestamp": "2025-05-14T11:10:00Z"}
                ]
            """.trimIndent(),
            timelineStr = """
                [
                  {"step": "Received", "time": "11:05 AM", "done": true},
                  {"step": "Qualified", "time": "11:10 AM", "done": true},
                  {"step": "Escalated", "time": "11:12 AM", "done": true, "active": true},
                  {"step": "Resolved", "time": null, "done": false}
                ]
            """.trimIndent()
        )
    )

    val initialEscalations = listOf(
        EscalationEntity(
            id = "esc_001",
            customer = "Sarah Jenkins",
            initials = "SJ",
            channel = "call",
            reason = "Delayed onboarding process. Mentions potential cancellation if not resolved by end of day. Requesting immediate call from management.",
            urgency = "high",
            receivedAt = "2025-05-24T11:40:00Z",
            sopMatch = "Onboarding Delay > 5 days → Escalate to Customer Success Lead"
        ),
        EscalationEntity(
            id = "esc_002",
            customer = "TechCorp Inc.",
            initials = "TC",
            channel = "email",
            reason = "Billing discrepancy on the latest invoice. Charged for an extra user license. Needs correction before the next billing cycle.",
            urgency = "medium",
            receivedAt = "2025-05-24T09:50:00Z",
            sopMatch = "Billing Error → Raise Ticket to Finance + Issue Credit Note"
        ),
        EscalationEntity(
            id = "esc_003",
            customer = "Global Logistics",
            initials = "GL",
            channel = "whatsapp",
            reason = "API integration failing for 3+ hours, blocking their primary workflow. Enterprise SLA breached. Technical account manager needs to step in.",
            urgency = "high",
            receivedAt = "2025-05-14T11:05:00Z",
            sopMatch = "Enterprise SLA Breach → Escalate to Engineering + Account Director"
        )
    )

    val initialFollowUps = listOf(
        FollowUpEntity(
            id = "fu_001",
            customer = "Sarah Jenkins",
            initials = "SJ",
            channel = "call",
            dueAt = "2025-05-24T15:00:00Z",
            messagePreview = "Following up on the proposal sent yesterday regarding the new marketing initiative. Needs final approval on budget.",
            done = false,
            overdue = false
        ),
        FollowUpEntity(
            id = "fu_002",
            customer = "Marcus Vance",
            initials = "MV",
            channel = "email",
            dueAt = "2025-05-24T13:00:00Z",
            messagePreview = "Check if they received the updated SLA document. They were concerned about the response time clauses.",
            done = false,
            overdue = true
        ),
        FollowUpEntity(
            id = "fu_003",
            customer = "Amanda Lewis",
            initials = "AL",
            channel = "call",
            dueAt = "2025-05-24T11:30:00Z",
            messagePreview = "Initial consultation call to discuss their current workflow bottlenecks.",
            done = true,
            overdue = false
        ),
        FollowUpEntity(
            id = "fu_004",
            customer = "James Rodriguez",
            initials = "JR",
            channel = "whatsapp",
            dueAt = "2025-05-24T16:30:00Z",
            messagePreview = "Send API documentation link and schedule technical review session with their engineering team.",
            done = false,
            overdue = false
        )
    )
}
